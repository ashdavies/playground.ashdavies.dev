package io.ashdavies.tally

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
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
import io.ashdavies.tally.home.HomeScreen

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    CanvasBasedWindow("Tally") {
        TallyApp()
    }
}

@Composable
private fun TallyApp() {
    MaterialTheme {
        val windowSize = WasmWindowSize(
            widthSizeClass = WindowWidthSizeClass.Compact,
            heightSizeClass = WindowHeightSizeClass.Compact
        )

        val tallyGraph = remember(windowSize) {
            val factory = createGraphFactory<WasmTallyGraph.Factory>()
            factory.create(windowSize)
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
