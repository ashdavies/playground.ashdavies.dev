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
import com.slack.circuit.Ui
import com.slack.circuit.presenterOf
import com.slack.circuit.ui
import io.ashdavies.playground.home.HomeScreen
import io.ashdavies.playground.profile.ProfileScreen

@Parcelize
public sealed interface EventsScreen : Parcelable, Screen {
    public object Profile : EventsScreen
    public object Home : EventsScreen
}

internal data class EventsState(
    val current: EventsScreen = EventsScreen.Home,
    val sink: (EventsEvent) -> Unit = { },
) : CircuitUiState

internal sealed interface EventsEvent : CircuitUiEvent {
    data class BottomNav(val screen: EventsScreen) : EventsEvent
}

@Composable
internal fun EventsPresenter(navigator: Navigator, screen: EventsScreen): EventsState = EventsState(screen) { event ->
    if (event is EventsEvent.BottomNav) navigator.resetRoot(event.screen)
}

public class EventsPresenterFactory : Presenter.Factory {
    override fun create(screen: Screen, navigator: Navigator, context: CircuitContext): Presenter<*>? = when (screen) {
        is EventsScreen.Profile -> presenterOf { EventsPresenter(navigator, EventsScreen.Profile) }
        is EventsScreen.Home -> presenterOf { EventsPresenter(navigator, EventsScreen.Home) }
        else -> null
    }
}

public class EventsUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is EventsScreen.Profile -> ui<EventsState> { state, modifier -> ProfileScreen(state, modifier) }
        is EventsScreen.Home -> ui<EventsState> { state, modifier -> HomeScreen(state, modifier) }
        else -> null
    }
}
