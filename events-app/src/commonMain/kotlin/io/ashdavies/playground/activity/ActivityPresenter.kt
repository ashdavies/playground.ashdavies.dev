package io.ashdavies.playground.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.playground.Event
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.events.rememberEventPager

@Parcelize
internal object ActivityScreen : Parcelable, Screen {
    sealed interface Event {
        data class Details(val eventId: String) : Event
    }

    data class State(val pagingItems: LazyPagingItems<io.ashdavies.playground.Event>) : CircuitUiState
}

@Composable
@OptIn(ExperimentalPagingApi::class, MultipleReferenceWarning::class)
internal fun ActivityPresenter(eventPager: Pager<String, Event> = rememberEventPager()): ActivityScreen.State {
    val pagingData = eventPager.flow.cachedIn(rememberCoroutineScope())
    return ActivityScreen.State(pagingData.collectAsLazyPagingItems())
}
