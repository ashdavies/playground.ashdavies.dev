package dev.ashdavies.playground

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.ashdavies.playground.material.dynamicColorScheme
import dev.zacsweers.metro.createGraphFactory

public fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Conference App",
        ) {
            ConferenceApp(
                context = PlatformContext,
                onClose = ::exitApplication,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun ConferenceApp(context: PlatformContext, onClose: () -> Unit) {
    MaterialExpressiveTheme(dynamicColorScheme()) {
        val conferenceGraph = remember(context) {
            val factory = createGraphFactory<JvmConferenceGraph.Factory>()
            factory.create(context)
        }

        CircuitCompositionLocals(conferenceGraph.circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(BottomBarScaffoldScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack) { onClose() },
                    backStack = backStack,
                    decoration = KeyNavigationDecoration(
                        decoration = conferenceGraph.circuit.defaultNavDecoration,
                        onBackInvoked = backStack::pop,
                    ),
                )
            }
        }
    }
}
