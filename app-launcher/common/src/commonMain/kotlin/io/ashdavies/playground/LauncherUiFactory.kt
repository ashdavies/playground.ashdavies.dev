package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitContext
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Screen
import com.slack.circuit.Ui
import com.slack.circuit.ui
import io.ashdavies.dominion.DominionRoot

public class LauncherUiFactory(private val componentContext: ComponentContext) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is LauncherScreen -> ui<LauncherState> { state, _ -> LauncherScreen(state) }
        is DominionScreen -> ui<CircuitUiState> { _, _ -> DominionRoot(componentContext) }
        is EventsScreen -> ui<CircuitUiState> { _, _ -> EventsRoot(componentContext) }
        else -> null
    }
}
