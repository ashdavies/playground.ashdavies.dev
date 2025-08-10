package dev.ashdavies.tally.upcoming

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.analytics.logEvent
import dev.ashdavies.paging.Pager
import dev.ashdavies.paging.rememberPagingState
import dev.ashdavies.tally.circuit.CircuitScreenKey
import dev.ashdavies.tally.coroutines.rememberRetainedCoroutineScope
import dev.ashdavies.tally.events.Event
import dev.ashdavies.tally.events.EventsDetailScreen
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding

internal class UpcomingPresenter @Inject constructor(
    @Assisted private val navigator: Navigator,
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
                is UpcomingScreen.Event.ItemClick -> {
                    remoteAnalytics.logEvent("events_click") { param("id", "${event.id}") }
                    navigator.goTo(EventsDetailScreen(event.id))
                }

                is UpcomingScreen.Event.Refresh -> {
                    remoteAnalytics.logEvent("events_refresh")
                    pagingState.refresh()
                }
            }
        }
    }

    @AssistedFactory
    @CircuitScreenKey(UpcomingScreen::class)
    @ContributesIntoMap(AppScope::class, binding<(Navigator) -> Presenter<*>>())
    interface Factory : (Navigator) -> UpcomingPresenter
}
