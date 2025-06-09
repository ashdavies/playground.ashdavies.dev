package io.ashdavies.tally.events.paging

import app.cash.sqldelight.paging3.QueryPagingSource
import io.ashdavies.tally.events.Conference
import io.ashdavies.tally.events.ConferenceQueries
import kotlinx.coroutines.Dispatchers

internal fun ConferencePagingSource(conferenceQueries: ConferenceQueries) = QueryPagingSource<Long, Conference>(
    transacter = conferenceQueries,
    context = Dispatchers.IO,
    pageBoundariesProvider = { anchor, limit ->
        conferenceQueries.pageBoundariesAscending(
            limit = limit,
            anchor = anchor,
        )
    },
    queryProvider = { beginInclusive, endExclusive ->
        conferenceQueries.keyedQueryAscending(
            beginInclusive = beginInclusive,
            endExclusive = endExclusive,
        )
    },
)
