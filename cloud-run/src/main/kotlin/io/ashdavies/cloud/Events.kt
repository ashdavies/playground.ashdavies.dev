package io.ashdavies.cloud

import com.google.cloud.firestore.Query
import io.ashdavies.playground.models.Event
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

private const val DEFAULT_LIMIT = 50

internal fun Route.events(json: Json = Json { ignoreUnknownKeys = true }) {
    get("/events") {
        val future = firestore
            .collection("events")
            .orderBy("dateStart", Query.Direction.DESCENDING)
            .limit(call.request.queryParameters["limit"]?.toInt() ?: DEFAULT_LIMIT)
            .get()

        val snapshot = future.await()
        val output = snapshot.map {
            val data = it.data
                .encode("cfp")
                .asJsonElement()

            json.decodeFromJsonElement(
                deserializer = Event.serializer(),
                element = data,
            )
        }

        call.respond(output)
    }

    post("/events:aggregate") {
        call.respond(HttpStatusCode.NotImplemented)
    }
}

private fun Map<*, *>.asJsonElement(): JsonElement = buildJsonObject {
    forEach { (key, value) ->
        when (value) {
            is Map<*, *> -> put(key as String, value.asJsonElement())
            is Boolean -> put(key as String, JsonPrimitive(value))
            is Number -> put(key as String, JsonPrimitive(value))
            is String -> put(key as String, JsonPrimitive(value))
            null -> put(key as String, JsonNull)
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun Map<String, Any?>.encode(prefix: String): Map<String, Any?> = buildMap {
    this@encode.forEach { entry ->
        when (entry.key.startsWith(prefix) && entry.value != null) {
            false -> put(entry.key, entry.value)
            true -> {
                val key = entry.key.drop(prefix.length).replaceFirstChar { it.lowercase() }
                val value = get(prefix) as? Map<String, Any?> ?: mutableMapOf()
                put(prefix, value + (key to entry.value))
            }
        }
    }
}
