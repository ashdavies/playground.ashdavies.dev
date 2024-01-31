package io.ashdavies.circuit

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen

public inline fun <reified UiScreen : Screen> presenterFactoryOf(
    crossinline body: @Composable (UiScreen, Navigator) -> CircuitUiState,
): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is UiScreen -> presenterOf { body(screen, navigator) }
        else -> null
    }
}
