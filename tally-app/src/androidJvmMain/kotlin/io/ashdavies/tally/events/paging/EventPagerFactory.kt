package io.ashdavies.tally.events.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.sqldelight.paging3.QueryPagingSource
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.EventsQueries
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

private object EventPagerDefaults {
    const val PAGE_SIZE = 10
}

@OptIn(ExperimentalPagingApi::class)
internal fun eventPager(
    eventsCallable: UpcomingEventsCallable,
    eventsQueries: EventsQueries,
    pageSize: Int = EventPagerDefaults.PAGE_SIZE,
    context: CoroutineContext = Dispatchers.IO,
): Pager<Long, Event> {
    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        QueryPagingSource<Long, Event>(
            transacter = eventsQueries,
            context = context,
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
        config = PagingConfig(pageSize),
        initialKey = null,
        remoteMediator = EventsRemoteMediator(
            eventsQueries = eventsQueries,
            eventsCallable = eventsCallable,
            onInvalidate = pagingSourceFactory::invalidate,
        ),
        pagingSourceFactory = pagingSourceFactory,
    )
}
