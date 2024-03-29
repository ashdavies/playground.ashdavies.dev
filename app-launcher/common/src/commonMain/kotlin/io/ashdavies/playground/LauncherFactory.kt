package io.ashdavies.playground

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf

internal fun launcherPresenterFactory(): Presenter.Factory {
    return presenterFactoryOf<LauncherScreen> { _, navigator ->
        LauncherPresenter(navigator)
    }
}

internal fun launcherUiFactory(): Ui.Factory {
    return uiFactoryOf<LauncherScreen, LauncherScreen.State> { _, state, modifier ->
        LauncherScreen(state, modifier)
    }
}
