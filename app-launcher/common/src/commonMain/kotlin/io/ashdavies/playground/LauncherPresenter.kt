package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.CircuitContext
import com.slack.circuit.CircuitUiEvent
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Navigator
import com.slack.circuit.Presenter
import com.slack.circuit.Screen
import io.ashdavies.dominion.DominionScreen
import kotlinx.collections.immutable.persistentListOf

@Parcelize
public object LauncherScreen : Parcelable, Screen

public data class LauncherState(val sink: (LauncherEvent) -> Unit = { }) : CircuitUiState

public sealed interface LauncherEvent : CircuitUiEvent {
    public object Dominion : LauncherEvent
    public object Events : LauncherEvent
}

@Composable
internal fun LauncherPresenter(navigator: Navigator): LauncherState = LauncherState { event ->
    when (event) {
        is LauncherEvent.Dominion -> navigator.goTo(DominionScreen)
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
        is DominionScreen -> Presenter { object : CircuitUiState {} }
        is EventsScreen -> Presenter { object : CircuitUiState {} }
        else -> null
    }
}

public fun buildInitialBackStack(initialScreen: String? = null): List<Screen> {
    return when (initialScreen) {
        "dominion" -> persistentListOf(LauncherScreen, DominionScreen)
        "events" -> persistentListOf(LauncherScreen, EventsScreen)
        else -> persistentListOf(LauncherScreen)
    }
}
