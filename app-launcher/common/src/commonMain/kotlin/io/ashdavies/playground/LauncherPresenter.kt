package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.CircuitContext
import com.slack.circuit.CircuitUiEvent
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Navigator
import com.slack.circuit.Presenter
import com.slack.circuit.Screen

public enum class LauncherRoute {
    Default, Dominion, Events,
}

public interface LauncherScreen : Screen

public data class LauncherState(
    val route: LauncherRoute = LauncherRoute.Default,
    val sink: (LauncherEvent) -> Unit = { },
) : CircuitUiState

public sealed interface LauncherEvent : CircuitUiEvent {
    public data class Click(val route: LauncherRoute) : LauncherEvent
}

@Composable
internal fun LauncherPresenter(initialRoute: LauncherRoute): LauncherState {
    var route by remember { mutableStateOf(initialRoute) }

    return LauncherState(route) { event ->
        when (event) {
            is LauncherEvent.Click -> route = event.route
        }
    }
}

public class LauncherPresenterFactory(
    private val initialRoute: LauncherRoute,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext
    ): Presenter<*>? = when (screen) {
        is LauncherScreen -> Presenter { LauncherPresenter(initialRoute) }
        else -> null
    }
}

private inline fun <UiState : CircuitUiState> Presenter(
    crossinline block: @Composable () -> UiState
): Presenter<UiState> = object : Presenter<UiState> {
    @Composable
    override fun present(): UiState = block()
}
