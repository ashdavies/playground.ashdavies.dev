package io.ashdavies.playground.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.network.todayAsString
import io.ashdavies.playground.rememberPlaygroundDatabase
import io.ktor.client.HttpClient

private const val DEFAULT_PAGE_SIZE = 10

@Composable
@ExperimentalPagingApi
@MultipleReferenceWarning
internal fun rememberEventPager(
    eventsQueries: EventsQueries = rememberPlaygroundDatabase().eventsQueries,
    httpClient: HttpClient = LocalHttpClient.current,
    initialKey: String = todayAsString(),
    pageSize: Int = DEFAULT_PAGE_SIZE,
): Pager<String, Event> = remember(eventsQueries, httpClient) {
    Pager(
        config = PagingConfig(pageSize),
        initialKey = initialKey,
        remoteMediator = EventsRemoteMediator(eventsQueries, httpClient),
        pagingSourceFactory = { EventsPagingSource(eventsQueries) },
    )
}
