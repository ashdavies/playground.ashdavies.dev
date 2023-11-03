package io.ashdavies.playground

import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

internal fun LauncherUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen is LauncherScreen) {
        true -> ui<LauncherScreen.State> { state, modifier -> LauncherScreen(state, modifier) }
        false -> null
    }
}
