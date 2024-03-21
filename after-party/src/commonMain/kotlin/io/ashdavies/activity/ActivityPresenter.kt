package io.ashdavies.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.events.Event
import io.ashdavies.events.rememberEventPager
import kotlinx.coroutines.CoroutineScope

@Composable
@OptIn(ExperimentalPagingApi::class)
internal fun ActivityPresenter(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    eventPager: Pager<String, Event> = rememberEventPager(),
): ActivityScreen.State {
    val pagingData = rememberRetained(coroutineScope) {
        eventPager.flow.cachedIn(coroutineScope)
    }

    return ActivityScreen.State(
        pagingItems = pagingData.collectAsLazyPagingItems(),
    )
}
