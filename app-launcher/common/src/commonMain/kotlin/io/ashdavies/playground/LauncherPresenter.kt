@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.dominion.DominionScreen
import io.ashdavies.gallery.GalleryScreen
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import io.ashdavies.playground.home.HomeScreen as EventsHomeScreen

private val DominionEntry = LauncherScreen.Entry(
    image = LauncherDrawableTokens.dominion,
    title = "Dominion",
    event = LauncherScreen.Event.Dominion,
)

private val EventsEntry = LauncherScreen.Entry(
    image = LauncherDrawableTokens.events,
    title = "Events",
    event = LauncherScreen.Event.Events,
)

private val GalleryEntry = LauncherScreen.Entry(
    image = LauncherDrawableTokens.gallery,
    title = "Gallery",
    event = LauncherScreen.Event.Gallery,
)

@Serializable
public object LauncherScreen : Screen {
    public data class Entry(
        val image: Any,
        val title: String,
        val event: Event,
    )

    public sealed interface Event : CircuitUiEvent {
        public data object Dominion : Event
        public data object Events : Event
        public data object Gallery : Event
    }

    public data class State(
        val entries: List<Entry> = emptyList(),
        val eventSink: (Event) -> Unit = { },
    ) : CircuitUiState
}

@Composable
internal fun LauncherPresenter(navigator: Navigator): LauncherScreen.State {
    val entries = listOf(DominionEntry, GalleryEntry, EventsEntry)

    return LauncherScreen.State(entries) { event ->
        when (event) {
            LauncherScreen.Event.Dominion -> navigator.goTo(DominionScreen.Home)
            LauncherScreen.Event.Events -> navigator.goTo(EventsHomeScreen)
            LauncherScreen.Event.Gallery -> navigator.goTo(GalleryScreen)
        }
    }
}

internal fun LauncherPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    if (screen is LauncherScreen) presenterOf { LauncherPresenter(navigator) } else null
}

public fun buildInitialBackStack(initialScreen: String? = null): List<Screen> {
    return when (initialScreen) {
        "dominion" -> persistentListOf(LauncherScreen, DominionScreen.Home)
        "events" -> persistentListOf(LauncherScreen, EventsHomeScreen)
        "gallery" -> persistentListOf(LauncherScreen, GalleryScreen)
        else -> persistentListOf(LauncherScreen)
    }
}
