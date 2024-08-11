package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import io.ashdavies.paging.collectAsLazyPagingItems
import io.ashdavies.party.paging.rememberRetainedCachedPagingFlow

@Composable
@OptIn(ExperimentalPagingApi::class)
internal fun EventsPresenter(
    eventPager: Pager<String, Event> = rememberEventPager(),
): EventsScreen.State {
    val pagingItems = eventPager.flow
        .rememberRetainedCachedPagingFlow()
        .collectAsLazyPagingItems()

    return EventsScreen.State(
        pagingItems = pagingItems,
        isLoading = pagingItems.loadState.isRefreshing,
    )
}


