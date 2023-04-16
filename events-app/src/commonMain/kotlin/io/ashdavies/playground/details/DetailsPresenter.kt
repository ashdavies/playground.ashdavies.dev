package io.ashdavies.playground.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen

@Parcelize
internal data class DetailsScreen(val eventId: String) : Parcelable, Screen {
    sealed interface Event : CircuitUiEvent {
        sealed interface NavEvent : Event {
            object Pop : NavEvent
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
