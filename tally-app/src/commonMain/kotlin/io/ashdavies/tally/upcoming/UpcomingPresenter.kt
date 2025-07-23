package io.ashdavies.tally.upcoming

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.Inject
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.analytics.logEvent
import io.ashdavies.paging.Pager
import io.ashdavies.paging.rememberPagingState
import io.ashdavies.tally.coroutines.rememberRetainedCoroutineScope
import io.ashdavies.tally.events.Event

internal class UpcomingPresenter @Inject constructor(
    private val eventPager: Pager<*, Event>,
    private val remoteAnalytics: RemoteAnalytics,
) : Presenter<UpcomingScreen.State> {

    @Composable
    override fun present(): UpcomingScreen.State {
        val pagingState = rememberPagingState(
            retainedCoroutineScope = rememberRetainedCoroutineScope(),
            pager = eventPager,
        )

        return UpcomingScreen.State(
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
    }
}
