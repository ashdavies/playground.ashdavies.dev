package io.ashdavies.routes

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelize

@Parcelize
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
