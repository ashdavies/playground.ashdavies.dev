package dev.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Navigator
import dev.ashdavies.material.dynamicColorScheme
import dev.ashdavies.playground.home.BottomBarScaffoldScreen

@Composable
public fun ConferenceApp(circuit: Circuit, navigator: Navigator, onClose: () -> Unit) {
    MaterialTheme(dynamicColorScheme()) {
        CircuitCompositionLocals(circuit) {
            ContentWithOverlays {
                NavigableCircuitContent(
                    navigator = navigator,
                    backStack = rememberSaveableBackStack(
                        root = BottomBarScaffoldScreen,
                    ),
                )
            }
        }
    }
}
