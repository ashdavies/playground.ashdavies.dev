package io.ashdavies.playground.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import app.cash.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.cachedIn
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.playground.Event
import io.ashdavies.playground.MultipleReferenceWarning
import io.ashdavies.playground.details.DetailsScreen
import io.ashdavies.playground.events.rememberEventPager
import kotlinx.serialization.Serializable

@Serializable
internal object ActivityScreen : Screen {
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
    val pagingData = eventPager.flow.cachedIn(rememberCoroutineScope())
    return ActivityScreen.State(pagingData.collectAsLazyPagingItems()) {
        if (it is ActivityScreen.Event.Details) navigator.goTo(DetailsScreen(it.eventId))
    }
}
