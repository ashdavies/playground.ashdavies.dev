package io.ashdavies.playground.events

import com.google.cloud.firestore.CollectionReference
import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.github.GitHubService
import io.ashdavies.playground.google.FirebaseFunction
import io.ashdavies.playground.graph
import io.ashdavies.playground.network.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("unused")
class EventsFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val collectionReference: CollectionReference = graph.collectionReference
        val json: Json = graph.json

        val eventsQuery = EventsQuery(request)
        val eventsReader = EventsReader(collectionReference, eventsQuery)
        val oldValue: Collection<Event> = eventsReader()

        val gitHubService: GitHubService = graph.gitHubService
        val eventsWriter = CollectionWriter(collectionReference, Event::id)
        //val newValue: Collection<Event> = gitHubService.getEvents()

        //eventsWriter(oldValue, newValue)
        return json.encodeToString(oldValue)
    }
}
