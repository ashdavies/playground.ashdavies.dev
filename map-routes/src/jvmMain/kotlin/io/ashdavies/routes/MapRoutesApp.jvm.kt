package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import io.ashdavies.content.PlatformContext
import io.ashdavies.playground.KeyNavigationDecoration

@Composable
internal fun MapRoutesApp(graph: RoutesGraph, onClose: () -> Unit) {
    val circuit = rememberCircuit(graph)

    CircuitCompositionLocals(circuit) {
        val backStack = rememberSaveableBackStack(RoutesScreen)

        NavigableCircuitContent(
            navigator = rememberCircuitNavigator(backStack) { onClose() },
            backStack = backStack,
            decoration = KeyNavigationDecoration(
                decoration = circuit.defaultNavDecoration,
                onBackInvoked = backStack::pop,
            ),
        )
    }
}

public fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(450.dp, 975.dp)),
            title = "MapRoutes",
        ) {
            MapRoutesApp(
                graph = createRoutesGraph(PlatformContext.Default),
                onClose = ::exitApplication,
            )
        }
    }
}
