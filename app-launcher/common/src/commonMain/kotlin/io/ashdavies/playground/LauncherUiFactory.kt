package io.ashdavies.playground

import com.slack.circuit.CircuitContext
import com.slack.circuit.Screen
import com.slack.circuit.Ui
import com.slack.circuit.ui

public class LauncherUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is LauncherScreen -> ui<LauncherState> { state, modifier -> LauncherScreen(state, modifier) }
        else -> null
    }
}
