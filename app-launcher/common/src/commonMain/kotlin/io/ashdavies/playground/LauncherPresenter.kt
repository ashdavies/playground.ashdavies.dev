package io.ashdavies.playground

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
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

private val RatingsEntry = LauncherScreen.Entry(
    event = LauncherScreen.Event.Ratings,
    image = Icons.Filled.Stars,
    text = "Ratings",
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
        public object Ratings : Event
    }

    public data class State(
        val entries: List<Entry> = emptyList(),
        val eventSink: (Event) -> Unit = { },
    ) : CircuitUiState
}

@Composable
internal fun LauncherPresenter(navigator: Navigator): LauncherScreen.State {
    val entries = listOf(DominionEntry, EventsEntry, RatingsEntry)

    return LauncherScreen.State(entries) { event ->
        when (event) {
            LauncherScreen.Event.Events -> navigator.goTo(EventsHomeScreen)
            LauncherScreen.Event.Dominion -> navigator.goTo(DominionScreen.Home)
            LauncherScreen.Event.Ratings -> navigator.goTo(RatingsScreen)
        }
    }
}

internal fun LauncherPresenterFactory() = Presenter.Factory { screen, navigator, _ ->
    if (screen is LauncherScreen) presenterOf { LauncherPresenter(navigator) } else null
}

public fun buildInitialBackStack(initialScreen: String? = null): List<Screen> {
    return when (initialScreen) {
        "events" -> persistentListOf(LauncherScreen, EventsHomeScreen)
        "dominion" -> persistentListOf(LauncherScreen, DominionScreen.Home)
        "ratings" -> persistentListOf(LauncherScreen, RatingsScreen)
        else -> persistentListOf(LauncherScreen)
    }
}
