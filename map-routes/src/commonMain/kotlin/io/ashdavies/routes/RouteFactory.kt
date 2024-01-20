package io.ashdavies.routes

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

public fun RoutePresenterFactory(): Presenter.Factory = Presenter.Factory { screen, navigator, _ ->
    when (screen) {
        is RouteScreen -> presenterOf { RoutePresenter() }
        else -> null
    }
}

public fun RouteUiFactory(): Ui.Factory = Ui.Factory { screen, _ ->
    when (screen) {
        is RouteScreen -> ui<RouteScreen.State> { state, modifier ->
            RouteScreen(state, modifier)
        }

        else -> null
    }
}
