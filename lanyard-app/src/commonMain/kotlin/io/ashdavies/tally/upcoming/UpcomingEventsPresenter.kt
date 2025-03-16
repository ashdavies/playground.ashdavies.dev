package io.ashdavies.lanyard.upcoming

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.lanyard.events.Event
import io.ashdavies.lanyard.events.paging.errorMessage
import io.ashdavies.lanyard.events.paging.isRefreshing
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun UpcomingEventsPresenter(
    eventPager: Pager<String, Event>,
): UpcomingEventsScreen.State {
    val coroutineScope = rememberCoroutineScope()
    val pagingItems = rememberRetained(coroutineScope) {
        eventPager.flow.cachedIn(coroutineScope)
    }.collectAsLazyPagingItems()

    return UpcomingEventsScreen.State(
        itemList = pagingItems
            .itemSnapshotList
            .toImmutableList(),
        selectedIndex = null,
        isRefreshing = pagingItems.loadState.isRefreshing,
        errorMessage = pagingItems.loadState.errorMessage,
    ) { event ->
        when (event) {
            is UpcomingEventsScreen.Event.Refresh -> pagingItems.refresh()
        }
    }
}
