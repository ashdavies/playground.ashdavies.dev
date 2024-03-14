package io.ashdavies.routes

import androidx.compose.runtime.remember
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import io.ashdavies.circuit.presenterFactoryOf
import io.ashdavies.circuit.uiFactoryOf
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn

public fun routePresenterFactory(context: PlatformContext): Presenter.Factory {
    return presenterFactoryOf<RouteScreen> { _, _ ->
        RoutePresenter(remember(context) { LocationService(context) })
    }
}

public fun routeUiFactory(context: PlatformContext): Ui.Factory {
    return uiFactoryOf<RouteScreen, RouteScreen.State> { _, state, modifier ->
        RouteScreen(state, modifier) { state.eventSink(RouteScreen.Event.OnEndPosition(it)) }
        context.reportFullyDrawn()
    }
}
