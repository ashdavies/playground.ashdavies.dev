package io.ashdavies.playground.aggregator

import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import io.ashdavies.cloud.CollectionQuery
import io.ashdavies.cloud.CollectionReader
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsSerializer
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.rememberDocumentProvider
import kotlin.jvm.optionals.getOrNull

private const val COLLECTION_PATH = "events"
private const val OK = "OK"

internal class AggregatorFunction : HttpFunction by HttpApplication({
    val provider = rememberDocumentProvider(COLLECTION_PATH)
    val request = LocalHttpRequest.current
    val service = GitHubService()

    HttpEffect {
        val reader = CollectionReader<Event>(provider, CollectionQuery(request))
        val writer = CollectionWriter(provider, Event::id)
        writer(reader(EventsSerializer), service.getEvents())
        OK
    }
})

private fun CollectionQuery(request: HttpRequest, defaults: CollectionQuery = CollectionQuery()) = CollectionQuery(
    limit = request.getFirstQueryParameter("limit").getOrNull()?.toInt() ?: defaults.limit,
    orderBy = request.getFirstQueryParameter("orderBy").getOrNull() ?: defaults.orderBy,
    startAt = request.getFirstQueryParameter("startAt").getOrNull(),
)
