package dev.ashdavies.cloud.events

import dev.ashdavies.cloud.google.FirestoreWriter
import dev.ashdavies.http.common.models.DomainEvent
import dev.ashdavies.http.common.models.DomainWrapper
import dev.zacsweers.metro.Inject
import io.ktor.util.logging.KtorSimpleLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToStringMap
import okio.ByteString.Companion.encodeUtf8
import kotlin.coroutines.CoroutineContext

private val Logger = KtorSimpleLogger("dev.ashdavies.cloud.events.FirebaseEventWriter")

public val DomainEvent.index: String
    get() = "$name-$location-$dateStart"

internal class FirebaseEventWriter(
    private val provider: FirestoreWriter.Provider,
    private val context: CoroutineContext,
) {

    @Inject
    constructor(provider: FirestoreWriter.Provider) : this(
        provider = provider,
        context = Dispatchers.IO,
    )

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun write(source: EventAggregateSource, value: List<DomainWrapper<DomainEvent>>) = withContext(context) {
        provider("events").use { writer ->
            val oldEventsById = writer.getWhereEqualTo("_source", source.name)
            val newEventsById = value.associateBy { it.value.index.hash() }

            for ((key, value) in newEventsById) {
                val hash = value.hash()

                if (key in oldEventsById.keys && hash == oldEventsById[key]?.get("_hash")) {
                    Logger.debug("Skipping '$key' found in existing events")
                    continue
                }

                val payload = Properties.encodeToStringMap(value.value) + mapOf(
                    "_attrs" to value.attrs,
                    "_source" to source.name,
                    "_hash" to hash,
                )

                Logger.info("Writing '$key'")
                writer.set(key, payload)
            }

            for (key in oldEventsById.keys) {
                if (key !in newEventsById.keys) {
                    writer.delete(key)
                }
            }
        }
    }
}

private inline fun <reified T : Any> T.hash() = Json
    .encodeToString(this)
    .hash()

private fun String.hash() = encodeUtf8()
    .md5()
    .hex()
