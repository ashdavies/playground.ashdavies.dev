package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.parcelable.Parcelize

@Parcelize
public object RouteScreen : Screen {
    public sealed interface Event : CircuitUiEvent

    internal data class State(
        val mapState: RouteMapState,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
internal fun RouteScreen(
    state: RouteScreen.State,
    modifier: Modifier = Modifier,
) {
    RouteMap(
        state = state.mapState,
        modifier = modifier,
    )
}
