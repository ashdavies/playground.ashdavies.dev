package dev.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.zacsweers.metro.createGraphFactory

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    ComposeViewport("ConferenceApp") {
        ConferenceApp(PlatformContext)
    }
}

@Composable
private fun ConferenceApp(context: PlatformContext) {
    MaterialTheme {
        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        val windowSizeClass = calculateWindowSizeClass()

        val conferenceGraph = remember(context, windowSizeClass) {
            val factory = createGraphFactory<WasmConferenceGraph.Factory>()
            factory.create(context, windowSizeClass)
        }

        CircuitCompositionLocals(conferenceGraph.circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(BottomBarScaffoldScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack) { },
                    backStack = backStack,
                )
            }
        }
    }
}
