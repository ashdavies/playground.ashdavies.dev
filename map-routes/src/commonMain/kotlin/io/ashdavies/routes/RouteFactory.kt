package io.ashdavies.routes

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.presenter.presenterOf
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import io.ashdavies.content.PlatformContext

public fun RoutePresenterFactory(context: PlatformContext): Presenter.Factory {
    val locationService = LocationService(context)

    return Presenter.Factory { screen, _, _ ->
        when (screen) {
            is RouteScreen -> presenterOf { RoutePresenter(locationService) }
            else -> null
        }
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
