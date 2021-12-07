package io.ashdavies.playground.aggregator

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.events.CollectionReference
import io.ashdavies.playground.events.EventsQuery
import io.ashdavies.playground.events.EventsReader
import io.ashdavies.playground.google.FirebaseFunction

private const val OK = "OK"

@Suppress("unused")
class AggregatorFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val collectionReference = CollectionReference()
        val gitHubService = GitHubService()

        val eventsReader = EventsReader(
            reference = collectionReference,
            request = EventsQuery(request),
        )

        val eventsWriter = CollectionWriter(
            reference = collectionReference,
            identifier = Event::id,
        )

        eventsWriter(
            newValue = gitHubService.getEvents(),
            oldValue = eventsReader(),
        )

        return OK
    }
}
