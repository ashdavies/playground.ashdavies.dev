package io.ashdavies.playground

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public fun RatingsPresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    if (screen is RatingsScreen) presenterOf { RatingsPresenter(navigator) } else null
}

public fun RatingsUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    if (screen is RatingsScreen) ui<RatingsScreen.State> { state, modifier -> RatingsScreen(state, modifier) } else null
}
