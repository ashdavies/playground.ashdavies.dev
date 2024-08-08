package io.ashdavies.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.network.todayAsString
import io.ashdavies.party.rememberLocalQueries

private const val DEFAULT_PAGE_SIZE = 10

@Composable
@ExperimentalPagingApi
internal fun rememberEventPager(
    eventsQueries: EventsQueries = rememberLocalQueries { it.eventsQueries },
    eventsCallable: GetUpcomingEventsCallable = GetUpcomingEventsCallable(LocalHttpClient.current),
    initialKey: String = todayAsString(),
    pageSize: Int = DEFAULT_PAGE_SIZE,
): Pager<String, Event> = remember(eventsQueries, eventsCallable) {
    val pagingSourceFactory = InvalidatingPagingSourceFactory {
        EventsPagingSource(eventsQueries)
    }

    val remoteMediator = EventsRemoteMediator(
        eventsQueries = eventsQueries,
        eventsCallable = eventsCallable,
        onInvalidate = pagingSourceFactory::invalidate,
    )

    Pager(
        config = PagingConfig(pageSize),
        initialKey = initialKey,
        remoteMediator = remoteMediator,
        pagingSourceFactory = pagingSourceFactory,
    )
}
