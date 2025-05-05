package io.ashdavies.tally

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dev.zacsweers.metro.createGraphFactory
import io.ashdavies.content.PlatformContext
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.playground.KeyNavigationDecoration
import io.ashdavies.tally.home.HomeScreen

public fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Tally",
        ) {
            TallyApp(
                context = PlatformContext.Default,
                onClose = ::exitApplication,
            )
        }
    }
}

@Composable
private fun TallyApp(
    context: PlatformContext,
    onClose: () -> Unit,
) {
    MaterialTheme(dynamicColorScheme()) {
        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        val windowSizeClass = calculateWindowSizeClass()

        val tallyGraph = remember(context, windowSizeClass) {
            val factory = createGraphFactory<JvmTallyGraph.Factory>()
            factory.create(context, windowSizeClass)
        }

        CircuitCompositionLocals(tallyGraph.circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(HomeScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack) { onClose() },
                    backStack = backStack,
                    decoration = KeyNavigationDecoration(
                        decoration = tallyGraph.circuit.defaultNavDecoration,
                        onBackInvoked = backStack::pop,
                    ),
                )
            }
        }
    }
}
