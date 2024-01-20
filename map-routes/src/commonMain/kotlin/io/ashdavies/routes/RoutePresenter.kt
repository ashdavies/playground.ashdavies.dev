package io.ashdavies.routes

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiState

@Composable
internal fun RoutePresenter(): CircuitUiState {
    return RouteScreen.State(
        eventSink = {},
    )
}
