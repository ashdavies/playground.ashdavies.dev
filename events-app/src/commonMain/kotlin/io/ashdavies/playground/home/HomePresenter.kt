package io.ashdavies.playground.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Navigator
import com.slack.circuit.Screen
import io.ashdavies.playground.EventsScreen
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.events.rememberEventPager
import io.ashdavies.playground.kotlin.asCloseableFlow

@Parcelize
public object HomeScreen : Parcelable, Screen {
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
internal fun HomePresenter(navigator: Navigator): HomeScreen.State {
    val eventPager = rememberEventPager()
    val pagingData = eventPager.flow
        .cachedIn(rememberCoroutineScope())
        .asCloseableFlow()

    return HomeScreen.State(pagingData.collectAsLazyPagingItems()) { event ->
        when (event) {
            is HomeScreen.Event.Details -> navigator.goTo(EventsScreen.Details(event.eventId))
            is HomeScreen.Event.BottomNav -> navigator.resetRoot(event.screen)
        }
    }
}
