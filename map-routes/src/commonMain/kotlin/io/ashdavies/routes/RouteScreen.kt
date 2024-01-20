package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen

@Parcelize
public object RouteScreen : Screen {
    public sealed interface Event : CircuitUiEvent

    internal data class State(
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState
}

@Composable
internal fun RouteScreen(
    state: RouteScreen.State,
    modifier: Modifier = Modifier,
) {
    RouteMap()
}
