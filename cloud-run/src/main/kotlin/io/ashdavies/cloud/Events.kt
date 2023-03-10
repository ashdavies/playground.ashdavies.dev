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
            json.decodeFromJsonElement(
                deserializer = Event.serializer(),
                element = it.data.asJsonElement(),
            )
        }

        call.respond(output)
    }

    post("/events:aggregate") {
        call.respond(HttpStatusCode.NotImplemented)
    }
}

private fun Map<String, Any?>.asJsonElement(): JsonElement = buildJsonObject {
    forEach { (key, value) ->
        when (value) {
            is Boolean -> put(key, JsonPrimitive(value))
            is Number -> put(key, JsonPrimitive(value))
            is String -> put(key, JsonPrimitive(value))
            null -> put(key, JsonNull)
        }
    }
}
