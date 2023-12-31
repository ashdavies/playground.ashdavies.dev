package io.ashdavies.playground

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

internal fun LauncherPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    if (screen is LauncherScreen) presenterOf { LauncherPresenter(navigator) } else null
}

internal fun LauncherUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen is LauncherScreen) {
        true -> ui<LauncherScreen.State> { state, modifier -> LauncherScreen(state, modifier) }
        false -> null
    }
}
