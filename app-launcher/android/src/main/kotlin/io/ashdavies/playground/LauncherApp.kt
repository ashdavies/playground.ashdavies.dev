package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.push
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Screen
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
