package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.google.FirebaseFunction
import io.ashdavies.playground.graph
import io.ashdavies.playground.network.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class EventsFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val json: Json = graph.json

        val eventsReader = EventsReader(
            request = EventsQuery(request),
            provider = DocumentProvider(),
        )

        return json.encodeToString(eventsReader())
    }
}
