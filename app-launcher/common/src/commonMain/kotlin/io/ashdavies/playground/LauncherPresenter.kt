package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.slack.circuit.CircuitContext
import com.slack.circuit.CircuitUiEvent
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Navigator
import com.slack.circuit.Presenter
import com.slack.circuit.Screen

public interface LauncherScreen : Screen

public data class LauncherState(val sink: (LauncherEvent) -> Unit = { }) : CircuitUiState

public sealed interface LauncherEvent : CircuitUiEvent {
    public object Events : LauncherEvent
    public object Dominion : LauncherEvent
}

@Composable
internal fun LauncherPresenter(navigator: Navigator): LauncherState = LauncherState { event ->
    when (event) {
        is LauncherEvent.Events -> navigator.goTo(EventsScreen)
        else -> Unit
    }
}

public class LauncherPresenterFactory : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = when (screen) {
        is LauncherScreen -> Presenter { LauncherPresenter(navigator) }
        else -> null
    }
}
