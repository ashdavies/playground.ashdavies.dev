package io.ashdavies.tally.upcoming

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.analytics.logEvent
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.paging.errorMessage
import io.ashdavies.tally.events.paging.isRefreshing
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun UpcomingPresenter(
    eventPager: Pager<Long, Event>,
    remoteAnalytics: RemoteAnalytics,
): UpcomingScreen.State {
    val coroutineScope = rememberCoroutineScope()
    val pagingItems = rememberRetained(coroutineScope) {
        eventPager.flow.cachedIn(coroutineScope)
    }.collectAsLazyPagingItems()

    return UpcomingScreen.State(
        itemList = pagingItems
            .itemSnapshotList
            .toImmutableList(),
        selectedIndex = null,
        isRefreshing = pagingItems.loadState.isRefreshing,
        errorMessage = pagingItems.loadState.errorMessage,
    ) { event ->
        when (event) {
            is UpcomingScreen.Event.Refresh -> {
                remoteAnalytics.logEvent("events_refresh")
                pagingItems.refresh()
            }
        }
    }
}
