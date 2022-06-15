package io.ashdavies.playground.aggregator

import com.google.cloud.functions.HttpFunction
import io.ashdavies.playground.Event
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.rememberDocumentProvider
import io.ashdavies.playground.events.EventsQuery
import io.ashdavies.playground.events.EventsReader

private const val COLLECTION_PATH = "events"
private const val OK = "OK"

internal class AggregatorFunction : HttpFunction by HttpApplication({
    val provider = rememberDocumentProvider(COLLECTION_PATH)
    val request = LocalHttpRequest.current
    val service = GitHubService()

    HttpEffect {
        val reader = EventsReader(provider, EventsQuery(request))
        val writer = CollectionWriter(provider, Event::id)
        writer(reader(), service.getEvents())
        OK
    }
})
