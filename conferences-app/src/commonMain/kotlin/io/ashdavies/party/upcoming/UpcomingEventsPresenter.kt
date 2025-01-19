package io.ashdavies.party.upcoming

import androidx.compose.runtime.Composable
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.party.events.Event
import io.ashdavies.party.events.paging.errorMessage
import io.ashdavies.party.events.paging.isRefreshing
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun UpcomingEventsPresenter(
    eventPager: Pager<String, Event>,
    coroutineScope: CoroutineScope,
): UpcomingEventsScreen.State {
    val pagingItems = rememberRetained(coroutineScope) {
        eventPager.flow.cachedIn(coroutineScope)
    }.collectAsLazyPagingItems()

    return UpcomingEventsScreen.State(
        itemList = pagingItems
            .itemSnapshotList
            .toPersistentList(),
        selectedIndex = null,
        isRefreshing = pagingItems.loadState.isRefreshing,
        errorMessage = pagingItems.loadState.errorMessage,
    ) { event ->
        if (event is UpcomingEventsScreen.Event.Refresh) {
            pagingItems.refresh()
        }
    }
}
