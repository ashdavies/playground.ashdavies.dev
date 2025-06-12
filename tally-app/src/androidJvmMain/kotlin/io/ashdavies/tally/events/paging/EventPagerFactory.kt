package io.ashdavies.tally.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.sqldelight.paging3.QueryPagingSource
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.EventsQueries
import kotlinx.coroutines.Dispatchers

private const val DEFAULT_PAGE_SIZE = 10

@OptIn(ExperimentalPagingApi::class)
internal fun eventPager(
    eventsCallable: UpcomingEventsCallable,
    eventsQueries: EventsQueries,
): Pager<Long, Event> {
    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        QueryPagingSource<Long, Event>(
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
    }

    return Pager(
        config = PagingConfig(DEFAULT_PAGE_SIZE),
        initialKey = 0L,
        remoteMediator = EventsRemoteMediator(
            eventsQueries = eventsQueries,
            eventsCallable = eventsCallable,
            onInvalidate = pagingSourceFactory::invalidate,
        ),
        pagingSourceFactory = pagingSourceFactory,
    )
}
