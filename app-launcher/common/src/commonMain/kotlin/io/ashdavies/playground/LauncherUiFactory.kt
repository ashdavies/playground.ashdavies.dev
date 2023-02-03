package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitContext
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Screen
import com.slack.circuit.Ui
import io.ashdavies.dominion.DominionRoot
import io.ashdavies.dominion.DominionScreen

public class LauncherUiFactory(private val componentContext: ComponentContext) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is LauncherScreen -> Ui<LauncherState> { state, _ -> LauncherScreen(state) }
        is DominionScreen -> Ui<CircuitUiState> { _, _ -> DominionRoot(componentContext) }
        is EventsScreen -> Ui<CircuitUiState> { _, _ -> EventsRoot(componentContext) }
        else -> null
    }
}
