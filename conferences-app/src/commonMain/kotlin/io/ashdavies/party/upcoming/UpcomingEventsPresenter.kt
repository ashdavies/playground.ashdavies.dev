package io.ashdavies.party.upcoming

import androidx.compose.runtime.Composable
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.paging.collectAsLazyPagingItems
import io.ashdavies.party.events.Event
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
        pagingItems = pagingItems,
    )
}
