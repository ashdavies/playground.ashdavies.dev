package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitConfig
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.push
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Screen

@Composable
internal fun LauncherApp(circuitConfig: CircuitConfig, initialBackStack: List<Screen>, onRootPop: () -> Unit) {
    val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }
    val navigator = rememberCircuitNavigator(backStack, onRootPop)
    val colorScheme = dynamicColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        CircuitCompositionLocals(circuitConfig) {
            NavigableCircuitContent(navigator, backStack)
        }
    }
}
