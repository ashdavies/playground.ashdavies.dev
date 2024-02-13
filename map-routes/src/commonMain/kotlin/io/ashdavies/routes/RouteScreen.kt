package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelize

public fun RouteScreen(): Screen = RouteScreen

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
    modifier: Modifier = Modifier,
    onEndPosition: (LatLng) -> Unit,
) {
    RouteMap(
        state = state.mapState,
        modifier = modifier,
        onEndPosition = onEndPosition,
    )
}
