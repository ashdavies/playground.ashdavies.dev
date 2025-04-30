package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

@Composable
internal fun RoutesScreen(
    state: RoutesScreen.State,
    modifier: Modifier = Modifier,
) {
    RoutesMap(
        state = state.mapState,
        onEndPosition = { state.eventSink(RoutesScreen.Event.OnEndPosition(it)) },
        modifier = modifier,
    )
}
