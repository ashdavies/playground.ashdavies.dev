package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen
import io.ashdavies.dominion.dominionScreen
import io.ashdavies.routes.routeScreen
import playground.app_launcher.common.generated.resources.Res
import playground.app_launcher.common.generated.resources.dominion
import playground.app_launcher.common.generated.resources.routes

private enum class LauncherEntries : LauncherScreen.State.Item {
    Dominion {
        override val imageModel = Res.drawable.dominion
        override val screen = dominionScreen()
        override val title = "Dominion"
    },

    Routes {
        override val imageModel = Res.drawable.routes
        override val screen = routeScreen()
        override val title = "Routes"
    },
}

@Composable
internal fun LauncherPresenter(navigator: Navigator): LauncherScreen.State {
    return LauncherScreen.State(enumValues<LauncherEntries>().toList()) { event ->
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
