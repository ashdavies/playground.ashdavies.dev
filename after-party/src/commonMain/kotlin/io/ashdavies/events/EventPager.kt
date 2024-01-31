package io.ashdavies.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import io.ashdavies.compose.MultipleReferenceWarning
import io.ashdavies.compose.rememberPlaygroundDatabase
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.network.todayAsString

private const val DEFAULT_PAGE_SIZE = 10

@Composable
@ExperimentalPagingApi
@MultipleReferenceWarning
internal fun rememberEventPager(
    eventsQueries: EventsQueries = rememberPlaygroundDatabase().eventsQueries,
    eventsCallable: GetEventsCallable = GetEventsCallable(LocalHttpClient.current),
    initialKey: String = todayAsString(),
    pageSize: Int = DEFAULT_PAGE_SIZE,
): Pager<String, Event> = remember(eventsQueries, eventsCallable) {
    Pager(
        config = PagingConfig(pageSize),
        initialKey = initialKey,
        remoteMediator = EventsRemoteMediator(eventsQueries, eventsCallable),
        pagingSourceFactory = { EventsPagingSource(eventsQueries) },
    )
}
