package io.ashdavies.playground.events

import androidx.compose.runtime.Composable
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.Event
import io.ashdavies.playground.EventsQueries
import io.ashdavies.playground.LocalPlaygroundDatabase
import io.ashdavies.playground.android.ViewModel
import io.ashdavies.playground.android.viewModel
import io.ashdavies.playground.android.viewModelScope
import io.ashdavies.playground.kotlin.CloseableFlow
import io.ashdavies.playground.kotlin.asCloseableFlow
import io.ashdavies.playground.network.todayAsString
import io.ktor.client.HttpClient

private const val NETWORK_PAGE_SIZE = 10

@OptIn(ExperimentalPagingApi::class)
internal class EventsViewModel(
    private val queries: EventsQueries,
    service: EventsService,
) : ViewModel() {

    private val pager = Pager(
        remoteMediator = EventsRemoteMediator(service, queries),
        config = PagingConfig(NETWORK_PAGE_SIZE),
        initialKey = todayAsString(),
    ) { EventsPagingSource(queries) }

    val pagingData: CloseableFlow<PagingData<Event>>
        get() = pager.flow
            .cachedIn(viewModelScope)
            .asCloseableFlow()
}

@Composable
internal fun rememberEventsViewModel(
    queries: EventsQueries = LocalPlaygroundDatabase.current.eventsQueries,
    client: HttpClient = LocalHttpClient.current,
) = viewModel {
    EventsViewModel(
        service = EventsService(client),
        queries = queries,
    )
}
