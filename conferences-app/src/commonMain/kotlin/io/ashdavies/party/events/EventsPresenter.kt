package io.ashdavies.party.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.Pager
import androidx.paging.cachedIn
import io.ashdavies.paging.collectAsLazyPagingItems
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun EventsPresenter(
    eventPager: Pager<String, Event>,
    coroutineScope: CoroutineScope,
): EventsScreen.State {
    val pagingItems = remember(coroutineScope) {
        eventPager.flow.cachedIn(coroutineScope)
    }.collectAsLazyPagingItems()

    return EventsScreen.State(
        pagingItems = pagingItems,
    )
}
