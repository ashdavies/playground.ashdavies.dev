package io.ashdavies.cloud

import io.ashdavies.http.common.models.Event
import io.ashdavies.playground.github.GitHubService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiEvent(
    val id: String,
    val name: String,
    val website: String,
    val location: String,
    val status: String?,
    val online: Boolean?,
    val dateStart: String,
    val dateEnd: String,
    val cfpStart: String?,
    val cfpEnd: String?,
    val cfpSite: String?,
)

internal fun Route.events() {
    get("/events") {
        val startAt = call.request.queryParameters["startAt"] ?: todayAsString()
        val limit = call.request.queryParameters["limit"]?.toInt() ?: 50

        val provider = DocumentProvider { firestore.collection("events") }
        val query = CollectionQuery(orderBy = "dateStart", startAt, limit)
        val reader = CollectionReader<Event>(provider, query)

        call.respond(reader(Event.serializer()) { it.encode("cfp") })
    }

    post("/events:aggregate") {
        val provider = DocumentProvider { firestore.collection("events") }
        val reader = CollectionReader<ApiEvent>(provider, CollectionQuery(limit = 0))
        val writer = CollectionWriter(provider, ApiEvent::id)

        writer(reader.invoke(ApiEvent.serializer()), GitHubService.getEvents(::ApiEvent))
        call.respond(HttpStatusCode.OK)
    }
}

@Suppress("UNCHECKED_CAST")
internal fun Map<String, Any?>.encode(prefix: String): Map<String, Any?> = buildMap {
    this@encode.forEach { entry ->
        when (entry.key.startsWith(prefix) && entry.value != null) {
            false -> put(entry.key, entry.value)
            true -> {
                val value = getOrElse(prefix) { mutableMapOf<String, Any?>() } as Map<String, Any?>
                val key = entry.key.drop(prefix.length).replaceFirstChar { it.lowercase() }
                put(prefix, value + (key to entry.value))
            }
        }
    }
}

private fun todayAsString(): String = Clock.System
    .todayIn(TimeZone.currentSystemDefault())
    .toString()
