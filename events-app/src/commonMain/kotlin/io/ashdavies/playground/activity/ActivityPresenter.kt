package io.ashdavies.playground.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.Pager
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Navigator
import com.slack.circuit.Screen
import io.ashdavies.playground.Event
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.details.DetailsScreen
import io.ashdavies.playground.events.rememberEventPager
import io.ashdavies.playground.kotlin.asCloseableFlow

@Parcelize
internal object ActivityScreen : Parcelable, Screen {
    sealed interface Event {
        data class Details(val eventId: String) : Event
    }

    data class State(
        val pagingItems: LazyPagingItems<io.ashdavies.playground.Event>,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
@OptIn(ExperimentalPagingApi::class, MultipleReferenceWarning::class)
internal fun ActivityPresenter(
    navigator: Navigator,
    eventPager: Pager<String, Event> = rememberEventPager(),
): ActivityScreen.State {
    val pagingData = eventPager.flow
        .cachedIn(rememberCoroutineScope())
        .asCloseableFlow()

    return ActivityScreen.State(pagingData.collectAsLazyPagingItems()) {
        if (it is ActivityScreen.Event.Details) navigator.goTo(DetailsScreen(it.eventId))
    }
}
