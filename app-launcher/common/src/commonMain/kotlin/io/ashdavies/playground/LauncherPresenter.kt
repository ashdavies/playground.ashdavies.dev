package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.dominion.DominionScreen
import io.ashdavies.party.AfterPartyScreen
import io.ashdavies.routes.RouteScreen

private enum class LauncherEntries : LauncherScreen.State.Item {
    AfterParty {
        override val imageModel = LauncherDrawableTokens.afterParty
        override val screen = AfterPartyScreen()
        override val title = "After Party"
    },

    Dominion {
        override val imageModel = LauncherDrawableTokens.dominion
        override val screen = DominionScreen()
        override val title = "Dominion"
    },

    Routes {
        override val imageModel = LauncherDrawableTokens.routes
        override val screen = RouteScreen()
        override val title = "Routes"
    },
}

@Composable
internal fun LauncherPresenter(navigator: Navigator): LauncherScreen.State {
    return LauncherScreen.State(listOf(*enumValues<LauncherEntries>())) { event ->
        navigator.onNavEvent(event)
    }
}

@Composable
public fun rememberSaveableBackStack(nextScreenName: String? = null): SaveableBackStack {
    return rememberSaveableBackStack(LauncherScreen) {
        val nextScreen = screenOrNull(nextScreenName)
        if (nextScreen != null) push(nextScreen)
    }
}

private fun screenOrNull(name: String? = null): Screen? = name?.let {
    return enumValues<LauncherEntries>()
        .firstOrNull { it.name.lowercase() == name.lowercase() }
        ?.screen
}
