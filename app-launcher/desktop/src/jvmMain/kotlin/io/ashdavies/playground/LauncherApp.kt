package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.slack.circuit.CircuitCompositionLocals
import com.slack.circuit.CircuitConfig
import com.slack.circuit.NavigableCircuitContent
import com.slack.circuit.Screen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.push
import com.slack.circuit.rememberCircuitNavigator

@Composable
internal fun LauncherApp(circuitConfig: CircuitConfig, initialBackStack: List<Screen>) {
    val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }
    val navigator = rememberCircuitNavigator(backStack)

    MaterialTheme(colorScheme = dynamicColorScheme()) {
        CircuitCompositionLocals(circuitConfig) {
            NavigableCircuitContent(navigator, backStack)
        }
    }
}
