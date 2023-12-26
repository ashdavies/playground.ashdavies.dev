@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.dominion.DominionScreen
import io.ashdavies.gallery.GalleryScreen
import io.ashdavies.playground.home.HomeScreen as EventsHomeScreen

private val DominionEntry = LauncherScreen.Entry(
    imageModel = LauncherDrawableTokens.dominion,
    title = "Dominion",
    event = LauncherScreen.Event.Dominion,
)

private val EventsEntry = LauncherScreen.Entry(
    imageModel = LauncherDrawableTokens.events,
    title = "Events",
    event = LauncherScreen.Event.Events,
)

private val GalleryEntry = LauncherScreen.Entry(
    imageModel = LauncherDrawableTokens.gallery,
    title = "Gallery",
    event = LauncherScreen.Event.Gallery,
)

@Parcelize
public object LauncherScreen : Parcelable, Screen {
    public data class Entry(
        val imageModel: Any,
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

@Composable
public fun rememberSaveableBackStack(initialScreenName: String? = null): SaveableBackStack = rememberSaveableBackStack {
    when (val initialScreen = initialScreenOrNull(initialScreenName)) {
        is Screen -> listOf(LauncherScreen, initialScreen)
        else -> listOf(LauncherScreen)
    }.forEach(::push)
}

private fun initialScreenOrNull(name: String? = null): Screen? = name?.let {
    return when (it) {
        "dominion" -> DominionScreen.Home
        "events" -> EventsHomeScreen
        "gallery" -> GalleryScreen
        else -> null
    }
}
