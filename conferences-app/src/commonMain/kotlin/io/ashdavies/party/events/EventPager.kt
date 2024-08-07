package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.ExperimentalPagingApi
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.party.network.todayAsString
import io.ashdavies.party.sql.rememberLocalQueries

private const val PLAYGROUND_BASE_URL = "playground.ashdavies.dev"
private const val DEFAULT_PAGE_SIZE = 10

@Composable
@ExperimentalPagingApi
internal fun rememberEventPager(
    eventsQueries: EventsQueries = rememberLocalQueries { it.eventsQueries },
    eventsCallable: UpcomingEventsCallable = UpcomingEventsCallable(
        httpClient = LocalHttpClient.current,
        baseUrl = PLAYGROUND_BASE_URL,
    ),
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
