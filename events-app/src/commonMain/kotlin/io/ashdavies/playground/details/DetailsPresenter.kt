package io.ashdavies.playground.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import kotlinx.serialization.Serializable

@Serializable
internal data class DetailsScreen(val eventId: String) : Screen {
    sealed interface Event : CircuitUiEvent {
        sealed interface NavEvent : Event {
            data object Pop : NavEvent
        }
    }

    data class State(
        val event: io.ashdavies.playground.Event?,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
internal fun DetailsPresenter(
    navigator: Navigator,
    eventId: String,
    repository: DetailsRepository = rememberDetailsRepository(),
): DetailsScreen.State {
    val event by repository
        .getEvent(eventId)
        .collectAsState(initial = null)

    return DetailsScreen.State(event) {
        if (it is DetailsScreen.Event.NavEvent.Pop) navigator.pop()
    }
}
