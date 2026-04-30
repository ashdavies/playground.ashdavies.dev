package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.analytics.logEvent
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.paging.PagerFactory
import dev.ashdavies.paging.rememberPagingState
import dev.ashdavies.playground.coroutines.rememberRetainedCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject

private const val DEFAULT_PAGE_SIZE = 10

public class EventListPresenter @AssistedInject constructor(
    @Assisted private val screen: Screen,
    @Assisted private val navigator: Navigator,
    private val eventPagerFactory: PagerFactory<Long, Event>,
    private val remoteAnalytics: RemoteAnalytics,
) : Presenter<EventListState> {

    @Composable
    override fun present(): EventListState {
        // https://github.com/ZacSweers/metro/issues/2227
        require(screen is EventScreen.List)

        val pagingState = rememberPagingState(
            retainedCoroutineScope = rememberRetainedCoroutineScope(),
            pager = eventPagerFactory.create(
                config = PagerConfig(
                    initialKey = screen.initialKey,
                    pageSize = DEFAULT_PAGE_SIZE,
                ),
            ),
        )

        return EventListState(
            itemList = pagingState.itemList,
            selectedIndex = null,
            isRefreshing = pagingState.isRefreshing,
            errorMessage = pagingState.errorMessage,
        ) { event ->
            when (event) {
                is EventListState.Event.ItemClick -> {
                    remoteAnalytics.logEvent("events_click") { param("id", "${event.id}") }
                    navigator.goTo(EventScreen.Detail(event.id))
                }

                is EventListState.Event.Refresh -> {
                    remoteAnalytics.logEvent("events_refresh")
                    pagingState.refresh()
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(EventScreen.List::class, AppScope::class)
    public fun interface Factory {
        public fun invoke(screen: Screen, navigator: Navigator): EventListPresenter
    }
}
