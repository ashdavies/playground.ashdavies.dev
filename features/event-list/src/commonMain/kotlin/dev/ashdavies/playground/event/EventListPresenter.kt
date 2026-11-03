package dev.ashdavies.playground.event

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.produceRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.paging.PagerConfig
import dev.ashdavies.paging.PagerFactory
import dev.ashdavies.playground.coroutines.retainStableCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.uuid.Uuid

private const val DEFAULT_PAGE_SIZE = 10

@AssistedInject
internal class EventListPresenter(
    @Assisted private val screen: EventScreen.List,
    @Assisted private val navigator: Navigator,
    private val eventPagerFactory: PagerFactory<Int, Event>,
    private val eventListItemFactory: EventListItemFactory,
    private val remoteAnalytics: RemoteAnalytics,
) : Presenter<EventListState> {

    @Composable
    override fun present(): EventListState {
        var bookmarks by retain { mutableStateOf(emptySet<Uuid>()) }
        val coroutineScope = retainStableCoroutineScope()

        val itemList by produceRetainedState<Flow<PagingData<EventListState.Success.Item>>?>(null) {
            val pagingData = eventPagerFactory
                .create(PagerConfig(screen.initialKey, DEFAULT_PAGE_SIZE))
                .flow.cachedIn(coroutineScope)

            // TODO Insert separators for each month / year
            value = combine(pagingData, snapshotFlow { bookmarks }) { pagingData, bookmarks ->
                pagingData.map { eventListItemFactory(it, it.uuid in bookmarks) }
            }
        }

        val pagingItems = itemList?.collectAsLazyPagingItems() ?: return EventListState.Failure(
            icon = Icons.Outlined.CloudDownload,
            message = null,
        )

        val error = pagingItems.loadState.refresh
            .let { it as? LoadState.Error }
            ?.error

        return when {
            error != null -> {
                remoteAnalytics.recordException(error)

                EventListState.Failure(
                    icon = Icons.Outlined.CloudOff,
                    message = error.message
                        ?.substringAfterLast(":")
                        ?: error.message,
                )
            }

            else -> EventListState.Success(
                itemList = pagingItems,
                isRefreshing = pagingItems.loadState.refresh is LoadState.Loading,
            ) { event ->
                when (event) {
                    is EventListState.Success.Event.ToggleBookmark -> pagingItems[event.index]?.let { item ->
                        bookmarks = if (item.uuid in bookmarks) bookmarks - item.uuid else bookmarks + item.uuid
                    }

                    is EventListState.Success.Event.ItemClick -> pagingItems[event.index]?.let {
                        remoteAnalytics.logEvent("events_click") { param("id", "${it.uuid}") }
                        navigator.goTo(EventScreen.Detail(it.uuid))
                    }

                    is EventListState.Success.Event.Refresh -> {
                        remoteAnalytics.logEvent("events_refresh")
                        pagingItems.refresh()
                    }
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
