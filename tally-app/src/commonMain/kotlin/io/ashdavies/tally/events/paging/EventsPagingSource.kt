package io.ashdavies.tally.events.paging

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.EventsQueries
import kotlinx.coroutines.Dispatchers

internal typealias EventsPagingSource = PagingSource<Long, Event>

internal fun EventsPagingSource(eventsQueries: EventsQueries): EventsPagingSource = QueryPagingSource(
    transacter = eventsQueries,
    context = Dispatchers.IO,
    pageBoundariesProvider = { anchor, limit ->
        eventsQueries.pageBoundariesAscending(
            limit = limit,
            anchor = anchor,
        )
    },
    queryProvider = { beginInclusive, endExclusive ->
        eventsQueries.keyedQueryAscending(
            beginInclusive = beginInclusive,
            endExclusive = endExclusive,
        )
    },
)
