package io.ashdavies.playground.configuration

import io.ashdavies.playground.collection.CollectionCache
import io.ashdavies.playground.firebase.Admin
import io.ashdavies.playground.firebase.CollectionReference
import io.ashdavies.playground.firebase.FirestoreDataConverter
import io.ashdavies.playground.firebase.QueryDocumentSnapshot
import io.ashdavies.playground.firebase.SnapshotOptions
import io.ashdavies.playground.firebase.admin
import io.ashdavies.playground.store.Cache
import io.ashdavies.playground.store.Options
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromDynamic
import kotlin.LazyThreadSafetyMode.NONE

private const val CONFIGURATION = "configuration"

private val Admin.collectionReference: CollectionReference<Configuration>
    get() = firestore().collection(CONFIGURATION)

private val dataConverter: FirestoreDataConverter<Configuration>
        by lazy(NONE) { SerializationDataConverter(Configuration.serializer()) }

private val configurationCache: ConfigurationCache
        by lazy(NONE) { CollectionCache(admin.collectionReference, dataConverter) }

internal fun ConfigurationCache(): ConfigurationCache =
    configurationCache

internal typealias ConfigurationCache =
        Cache<Configuration.Type, Configuration>

internal suspend fun ConfigurationCache.getOrNull(): Configuration? =
    read(Configuration.Type, Options())

internal suspend fun ConfigurationCache.getOrDefault(): Configuration =
    getOrNull() ?: Configuration()

internal suspend fun ConfigurationCache.put(configuration: Configuration) =
    write(Configuration.Type, configuration)

@Serializable
data class Configuration(
    val gitHubToken: String? = null,
    val updatedAt: String? = null,
) {
    sealed class Type(override val name: String) : CollectionCache.Type {
        companion object Default : Type("default")
    }
}

private class SerializationDataConverter<T>(
    private val deserializer: DeserializationStrategy<T>,
) : FirestoreDataConverter<T> {

    @ExperimentalSerializationApi
    override fun fromFirestore(snapshot: QueryDocumentSnapshot<T>, options: SnapshotOptions): T {
        return Json.decodeFromDynamic(deserializer, snapshot.data().asDynamic())
    }
}