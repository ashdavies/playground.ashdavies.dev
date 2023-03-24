package io.ashdavies.playground

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.CircuitUiEvent
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Navigator
import com.slack.circuit.Presenter
import com.slack.circuit.Screen
import com.slack.circuit.presenterOf
import io.ashdavies.dominion.DominionScreen
import kotlinx.collections.immutable.persistentListOf
import io.ashdavies.playground.home.HomeScreen as EventsHomeScreen

private val DominionEntry = LauncherScreen.Entry(
    event = LauncherScreen.Event.Dominion,
    image = Icons.Filled.Grid4x4,
    text = "Dominion",
)

private val EventsEntry = LauncherScreen.Entry(
    event = LauncherScreen.Event.Events,
    image = Icons.Filled.Event,
    text = "Events",
)

@Parcelize
public object LauncherScreen : Parcelable, Screen {
    public data class Entry(
        val image: ImageVector,
        val event: Event,
        val text: String,
    )

    public sealed interface Event : CircuitUiEvent {
        public object Dominion : Event
        public object Events : Event
    }

    public data class State(
        val entries: List<Entry> = emptyList(),
        val eventSink: (Event) -> Unit = { },
    ) : CircuitUiState
}

@Composable
internal fun LauncherPresenter(
    navigator: Navigator,
) = LauncherScreen.State(listOf(DominionEntry, EventsEntry)) { event ->
    when (event) {
        is LauncherScreen.Event.Events -> navigator.goTo(EventsHomeScreen)
        is LauncherScreen.Event.Dominion -> navigator.goTo(DominionScreen.Home)
    }
}

internal fun LauncherPresenterFactory() = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is LauncherScreen -> presenterOf { LauncherPresenter(navigator) }
        else -> null
    }
}

public fun buildInitialBackStack(initialScreen: String? = null): List<Screen> {
    return when (initialScreen) {
        "events" -> persistentListOf(LauncherScreen, EventsHomeScreen)
        "dominion" -> persistentListOf(LauncherScreen, DominionScreen.Home)
        else -> persistentListOf(LauncherScreen)
    }
}
