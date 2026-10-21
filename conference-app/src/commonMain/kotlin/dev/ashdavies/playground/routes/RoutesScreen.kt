package dev.ashdavies.playground.routes

import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.serialization.CircuitSerializable
import dev.zacsweers.metro.AppScope
import kotlinx.serialization.Serializable

@Serializable
@CircuitSerializable(AppScope::class)
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
