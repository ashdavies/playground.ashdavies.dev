package io.ashdavies.playground.events

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.ashdavies.playground.database.Event
import io.ashdavies.playground.database.EventsQueries
import io.ashdavies.playground.network.EventsService
import kotlinx.coroutines.flow.Flow

private const val NETWORK_PAGE_SIZE = 10

/*
 * TODO https://developer.android.com/codelabs/android-paging#6
 */
@OptIn(ExperimentalPagingApi::class)
internal class EventsViewModel(private val queries: EventsQueries, service: EventsService) : ViewModel() {

    val pagingData: Flow<PagingData<Event>> = Pager(
        remoteMediator = EventsMediator(queries, service),
        pagingSourceFactory = { EventsSource(queries) },
        config = PagingConfig(NETWORK_PAGE_SIZE),
    ).flow
}
