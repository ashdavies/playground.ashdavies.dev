package io.ashdavies.playground.aggregator

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.Event
import io.ashdavies.playground.events.EventsQuery
import io.ashdavies.playground.events.EventsReader
import io.ashdavies.playground.firebase.DocumentProvider
import io.ashdavies.playground.firebase.FirebaseFunction

private const val COLLECTION_PATH = "events"
private const val OK = "OK"

internal class AggregatorFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val documentProvider = DocumentProvider(COLLECTION_PATH)
        val gitHubService = GitHubService()

        val eventsReader = EventsReader(
            request = EventsQuery(request),
            provider = documentProvider,
        )

        val eventsWriter = CollectionWriter(
            provider = documentProvider,
            identifier = Event::id,
        )

        eventsWriter(
            newValue = gitHubService.getEvents(),
            oldValue = eventsReader(),
        )

        return OK
    }
}
