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
public object ActivityScreen : Parcelable, Screen {
    internal sealed interface Event {
        data class BottomNav(val screen: Screen) : Event
        data class Details(val eventId: String) : Event
    }

    internal data class State(
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

    return ActivityScreen.State(pagingData.collectAsLazyPagingItems()) { event ->
        when (event) {
            is ActivityScreen.Event.Details -> navigator.goTo(DetailsScreen(event.eventId))
            is ActivityScreen.Event.BottomNav -> navigator.resetRoot(event.screen)
        }
    }
}
