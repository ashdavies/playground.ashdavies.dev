package dev.ashdavies.playground.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.retain.retain
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.analytics.logEvent
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.paging.PagerFactory
import dev.ashdavies.playground.coroutines.retainCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_PAGE_SIZE = 10

@AssistedInject
internal class EventListPresenter(
    @Assisted private val screen: EventScreen.List,
    @Assisted private val navigator: Navigator,
    private val eventPagerFactory: PagerFactory<Long, Event>,
    private val remoteAnalytics: RemoteAnalytics,
) : Presenter<EventListState> {

    @Composable
    override fun present(): EventListState {
        val eventPager by produceState<Pager<Long, Event>?>(null) {
            value = eventPagerFactory.create(PagerConfig(screen.initialKey, DEFAULT_PAGE_SIZE))
        }

        val pagingItems = eventPager?.let {
            val coroutineScope = retainCoroutineScope()
            val pagingData = retain { it.flow.cachedIn(coroutineScope) }
            pagingData.collectAsLazyPagingItems()
        } ?: return EventListState(
            itemList = persistentListOf(),
            selectedIndex = null,
            isRefreshing = true,
            errorMessage = null,
            eventSink = { },
        )

        return EventListState(
            itemList = pagingItems
                .itemSnapshotList
                .toImmutableList(),
            selectedIndex = null,
            isRefreshing = pagingItems.loadState.refresh is LoadState.Loading,
            errorMessage = pagingItems.loadState.refresh
                .let { it as? LoadState.Error }
                ?.error?.message,
        ) { event ->
            when (event) {
                is EventListState.Event.ItemClick -> {
                    remoteAnalytics.logEvent("events_click") { param("id", "${event.id}") }
                    navigator.goTo(EventScreen.Detail(event.id))
                }

                is EventListState.Event.Refresh -> {
                    remoteAnalytics.logEvent("events_refresh")
                    pagingItems.refresh()
                }
            }
        }
    }

    @AssistedFactory
    @CircuitInject(EventScreen.List::class, AppScope::class)
    fun interface Factory {
        fun invoke(screen: EventScreen.List, navigator: Navigator): EventListPresenter
    }
}
