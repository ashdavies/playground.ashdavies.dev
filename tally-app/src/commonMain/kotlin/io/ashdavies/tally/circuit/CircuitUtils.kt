package io.ashdavies.tally.circuit

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

internal inline fun <reified S : Screen, UiState : CircuitUiState> presenterFactoryOf(
    crossinline body: @Composable (Navigator, CircuitContext) -> UiState,
): Presenter.Factory = Presenter.Factory { screen, navigator, context ->
    when (screen) {
        is S -> presenterOf { body(navigator, context) }
        else -> null
    }
}

internal inline fun <reified S : Screen, UiState : CircuitUiState> uiFactoryOf(
    crossinline body: @Composable (state: UiState, modifier: Modifier) -> Unit,
): Ui.Factory = Ui.Factory { screen, context ->
    when (screen) {
        is S -> ui<UiState> { state, modifier -> body(state, modifier) }
        else -> null
    }
}
