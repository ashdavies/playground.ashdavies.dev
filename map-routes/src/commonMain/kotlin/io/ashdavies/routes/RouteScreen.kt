package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelize

@Parcelize
internal object RouteScreen : Screen {
    sealed interface Event : CircuitUiEvent {
        data class OnEndPosition(val position: LatLng) : Event
    }

    data class State(
        val mapState: RouteMapState = RouteMapState(),
        val errorMessage: String? = null,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
internal fun RouteScreen(
    state: RouteScreen.State,
    onEndPosition: (LatLng) -> Unit,
    modifier: Modifier = Modifier,
) {
    RouteMap(
        state = state.mapState,
        onEndPosition = onEndPosition,
        modifier = modifier,
    )
}
