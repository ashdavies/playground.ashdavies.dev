package io.ashdavies.playground

import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public class LauncherUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is LauncherScreen -> ui<LauncherScreen.State> { state, modifier ->
            LauncherScreen(state, modifier)
        }

        else -> null
    }
}
