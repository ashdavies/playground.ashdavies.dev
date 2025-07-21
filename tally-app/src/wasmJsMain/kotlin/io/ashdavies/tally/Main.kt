package io.ashdavies.tally

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dev.zacsweers.metro.createGraphFactory
import io.ashdavies.content.PlatformContext
import io.ashdavies.tally.home.HomeScreen

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    CanvasBasedWindow("Tally") {
        TallyApp(PlatformContext)
    }
}

@Composable
private fun TallyApp(context: PlatformContext) {
    MaterialTheme {
        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        val windowSizeClass = calculateWindowSizeClass()

        val tallyGraph = remember(context, windowSizeClass) {
            val factory = createGraphFactory<WasmTallyGraph.Factory>()
            factory.create(context, windowSizeClass)
        }

        CircuitCompositionLocals(tallyGraph.circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(HomeScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack) { },
                    backStack = backStack,
                )
            }
        }
    }
}
