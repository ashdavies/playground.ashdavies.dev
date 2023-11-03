package io.ashdavies.playground.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import io.ashdavies.generated.apis.EventsApi
import io.ashdavies.http.LocalHttpCredentials
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.network.todayAsString
import io.ashdavies.playground.rememberPlaygroundDatabase

private const val DEFAULT_PAGE_SIZE = 10

@Composable
@ExperimentalPagingApi
@MultipleReferenceWarning
internal fun rememberEventPager(
    eventsQueries: EventsQueries = rememberPlaygroundDatabase().eventsQueries,
    eventsApi: EventsApi = rememberEventsApi(),
    initialKey: String = todayAsString(),
    pageSize: Int = DEFAULT_PAGE_SIZE,
): Pager<String, Event> = remember(eventsQueries, eventsApi) {
    Pager(
        config = PagingConfig(pageSize),
        initialKey = initialKey,
        remoteMediator = EventsRemoteMediator(eventsQueries, eventsApi),
        pagingSourceFactory = { EventsPagingSource(eventsQueries) },
    )
}

@Composable
private fun rememberEventsApi(
    apiKey: String = LocalHttpCredentials.current.apiKey,
): EventsApi = remember(apiKey) {
    EventsApi().apply { setApiKey(apiKey) }
}
