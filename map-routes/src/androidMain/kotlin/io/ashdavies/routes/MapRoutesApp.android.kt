package io.ashdavies.routes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.content.enableStrictMode

@Composable
internal fun MapRoutesApp(graph: RoutesGraph) {
    CircuitCompositionLocals(rememberCircuit(graph)) {
        ContentWithOverlays {
            val backStack = rememberSaveableBackStack(RoutesScreen)

            NavigableCircuitContent(
                navigator = rememberCircuitNavigator(backStack),
                backStack = backStack,
            )
        }
    }
}

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        enableStrictMode(false)

        setContent {
            MapRoutesApp(createRoutesGraph(LocalContext.current))
        }
    }
}
