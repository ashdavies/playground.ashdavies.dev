package dev.ashdavies.playground.routes

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.serialization.Serializable

@Serializable
internal object RoutesScreen : Screen {
    sealed interface Event : CircuitUiEvent {
        data class OnEndPosition(val position: LatLng) : Event
    }

    data class State(
        val mapState: RoutesMapState,
        val errorMessage: String?,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}
