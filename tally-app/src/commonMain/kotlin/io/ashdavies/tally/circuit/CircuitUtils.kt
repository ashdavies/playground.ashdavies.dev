package io.ashdavies.tally.circuit

import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.Provider

internal inline fun <reified S : Screen, UiState : CircuitUiState> presenterFactoryOf(
    provider: Provider<out Presenter<UiState>>,
): Presenter.Factory = presenterFactoryOf<S, _> { _ -> provider() }

internal inline fun <reified S : Screen, UiState : CircuitUiState> presenterFactoryOf(
    crossinline factory: (Navigator) -> Presenter<UiState>,
): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is S -> factory(navigator)
        else -> null
    }
}

internal inline fun <reified S : Screen, UiState : CircuitUiState> uiFactoryOf(
    provider: Provider<out Ui<UiState>>,
): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is S -> provider()
        else -> null
    }
}
