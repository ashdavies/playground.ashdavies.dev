package io.ashdavies.playground.events

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.google.FirebaseFunction
import io.ashdavies.playground.graph
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private const val OK = "OK"

class EventsWriteFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val newValue: List<Event> = graph
            .gitHubService
            .getEvents()

        val reader = EventsReader(graph.collectionReference, EventsRequest(request))
        val writer = CollectionWriter(graph.collectionReference, Event::id)
        writer(reader(), newValue)
        return "OK"
    }
}

object EventsReadFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val reader = EventsReader(graph.collectionReference, EventsRequest(request))
        return Json.encodeToString(reader())
    }
}

