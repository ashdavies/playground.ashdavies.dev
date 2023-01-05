package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.slack.circuit.CircuitContext
import com.slack.circuit.CircuitUiState
import com.slack.circuit.Screen
import com.slack.circuit.ScreenUi
import com.slack.circuit.Ui
import io.ashdavies.dominion.DominionRoot
import io.ashdavies.dominion.DominionScreen

public class LauncherUiFactory(private val componentContext: ComponentContext) : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): ScreenUi? = when (screen) {
        is LauncherScreen -> ScreenUi(Ui<LauncherState> { LauncherScreen(it) })
        is DominionScreen -> ScreenUi(Ui<CircuitUiState> { DominionRoot(componentContext) })
        is EventsScreen -> ScreenUi(Ui<CircuitUiState> { EventsRoot(componentContext) })
        else -> null
    }
}
