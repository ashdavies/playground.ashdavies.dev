package io.ashdavies.tally.events.paging

import app.cash.sqldelight.paging3.QueryPagingSource
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.EventsQueries
import kotlinx.coroutines.Dispatchers

internal fun ConferencePagingSource(eventsQueries: EventsQueries) = QueryPagingSource<Long, Event>(
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
