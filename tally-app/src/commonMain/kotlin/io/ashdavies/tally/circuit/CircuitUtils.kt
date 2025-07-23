package io.ashdavies.tally.circuit

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen

internal inline fun <reified S : Screen, UiState : CircuitUiState> presenterFactoryOf(
    crossinline factory: (Navigator) -> Presenter<UiState>,
): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is S -> factory(navigator)
        else -> null
    }
}
