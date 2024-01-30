package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.content.PlatformContext

public fun RoutePresenterFactory(context: PlatformContext): Presenter.Factory {
    return presenterFactoryOf<RouteScreen, RouteScreen.State> { _, _ ->
        RoutePresenter(remember(context) { LocationService(context) })
    }
}

public fun RouteUiFactory(): Ui.Factory {
    return uiFactoryOf<RouteScreen, RouteScreen.State> { _, state, modifier ->
        RouteScreen(state, modifier)
    }
}

private inline fun <reified UiScreen : Screen, UiState : CircuitUiState> presenterFactoryOf(
    crossinline body: @Composable (UiScreen, Navigator) -> UiState,
): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    if (screen is UiScreen) presenterOf { body(screen, navigator) } else null
}

private inline fun <reified UiScreen : Screen, UiState : CircuitUiState> uiFactoryOf(
    crossinline body: @Composable (screen: UiScreen, state: UiState, modifier: Modifier) -> Unit,
): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is UiScreen -> ui<UiState> { state, modifier -> body(screen, state, modifier) }
        else -> null
    }
}
