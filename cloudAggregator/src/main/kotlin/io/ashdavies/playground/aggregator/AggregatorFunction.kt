package io.ashdavies.playground.aggregator

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.events.DocumentProvider
import io.ashdavies.playground.events.EventsQuery
import io.ashdavies.playground.events.EventsReader
import io.ashdavies.playground.google.FirebaseFunction

private const val OK = "OK"

internal class AggregatorFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val documentProvider = DocumentProvider()
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
