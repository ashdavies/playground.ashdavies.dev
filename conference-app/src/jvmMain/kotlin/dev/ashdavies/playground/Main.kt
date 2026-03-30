package dev.ashdavies.playground

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.zacsweers.metro.createGraphFactory

public fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Conference App",
        ) {
            ConferenceApp(
                circuit = createGraphFactory<JvmConferenceGraph.Factory>()
                    .create(PlatformContext)
                    .circuit,
                navigator = rememberCircuitNavigator(
                    backStack = rememberSaveableBackStack(BottomBarScaffoldScreen),
                    onRootPop = { exitApplication() },
                ),
                onClose = ::exitApplication,
            )
        }
    }
}
