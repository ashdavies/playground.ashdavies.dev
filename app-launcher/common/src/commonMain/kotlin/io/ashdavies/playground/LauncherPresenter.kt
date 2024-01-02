package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.dominion.DominionScreen
import io.ashdavies.party.AfterPartyScreen

private val AfterPartyEntry = LauncherScreen.Entry(
    imageModel = LauncherDrawableTokens.afterParty,
    title = "After Party",
    event = LauncherScreen.Event.AfterParty,
)

private val DominionEntry = LauncherScreen.Entry(
    imageModel = LauncherDrawableTokens.dominion,
    title = "Dominion",
    event = LauncherScreen.Event.Dominion,
)

@Composable
internal fun LauncherPresenter(navigator: Navigator): LauncherScreen.State {
    val entries = listOf(AfterPartyEntry, DominionEntry)

    return LauncherScreen.State(entries) { event ->
        when (event) {
            LauncherScreen.Event.AfterParty -> navigator.goTo(AfterPartyScreen)
            LauncherScreen.Event.Dominion -> navigator.goTo(DominionScreen.Home)
        }
    }
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
        "after-party" -> AfterPartyScreen
        "dominion" -> DominionScreen.Home
        else -> null
    }
}
