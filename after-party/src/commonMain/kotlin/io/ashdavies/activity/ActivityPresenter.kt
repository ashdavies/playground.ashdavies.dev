package io.ashdavies.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.events.Event
import io.ashdavies.events.rememberEventPager
import io.ashdavies.parcelable.Parcelable
import io.ashdavies.parcelable.Parcelize

@Composable
@OptIn(ExperimentalPagingApi::class)
internal fun ActivityPresenter(eventPager: Pager<String, Event> = rememberEventPager()): ActivityScreen.State {
    val pagingData = eventPager.flow.cachedIn(rememberCoroutineScope())
    return ActivityScreen.State(pagingData.collectAsLazyPagingItems())
}
