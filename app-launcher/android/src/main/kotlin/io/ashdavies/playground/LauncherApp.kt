package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.slack.circuit.CircuitCompositionLocals
import com.slack.circuit.CircuitConfig
import com.slack.circuit.NavigableCircuitContent
import com.slack.circuit.Screen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.push
import com.slack.circuit.rememberCircuitNavigator
import io.ashdavies.compose.ProvideFirebaseApp

@Composable
internal fun LauncherApp(circuitConfig: CircuitConfig, initialBackStack: List<Screen>) {
    val systemUiController = rememberSystemUiController()
    val colorScheme = dynamicColorScheme()

    val primaryContainer = colorScheme.primaryContainer
    systemUiController.setSystemBarsColor(primaryContainer)

    val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }
    val navigator = rememberCircuitNavigator(backStack)

    ProvideFirebaseApp {
        MaterialTheme(colorScheme = colorScheme) {
            CircuitCompositionLocals(circuitConfig) {
                ContentWithOverlays { NavigableCircuitContent(navigator, backStack) }
            }
        }
    }
}
