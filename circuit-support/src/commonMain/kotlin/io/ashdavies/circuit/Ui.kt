package io.ashdavies.circuit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public inline fun <reified UiScreen : Screen, UiState : CircuitUiState> uiFactoryOf(
    crossinline body: @Composable (screen: UiScreen, state: UiState, modifier: Modifier) -> Unit,
): Ui.Factory = Ui.Factory { screen, _ ->
    if (screen !is UiScreen) return@Factory null

    ui<UiState> { state, modifier ->
        body(screen, state, modifier)
    }
}
