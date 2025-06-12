package io.ashdavies.tally.upcoming

import androidx.compose.runtime.Composable
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.analytics.logEvent
import io.ashdavies.paging.PagingState
import io.ashdavies.tally.events.Event

@Composable
internal fun UpcomingPresenter(
    pagingState: PagingState<Event>,
    remoteAnalytics: RemoteAnalytics,
) = UpcomingScreen.State(
    itemList = pagingState.itemList,
    selectedIndex = null,
    isRefreshing = pagingState.isRefreshing,
    errorMessage = pagingState.errorMessage,
) { event ->
    when (event) {
        is UpcomingScreen.Event.Refresh -> {
            remoteAnalytics.logEvent("events_refresh")
            pagingState.refresh()
        }
    }
}
