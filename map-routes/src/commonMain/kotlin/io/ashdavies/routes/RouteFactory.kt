package io.ashdavies.routes

import androidx.compose.runtime.remember
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ashdavies.content.PlatformContext

public fun Circuit.Builder.addRoutePresenter(context: PlatformContext): Circuit.Builder {
    return addPresenter<RouteScreen, RouteScreen.State> { _, _, _ ->
        presenterOf { RoutePresenter(remember(context) { LocationService(context) }) }
    }
}

public fun Circuit.Builder.addRouteUi(): Circuit.Builder {
    return addUi<RouteScreen, RouteScreen.State> { state, modifier ->
        RouteScreen(
            state = state,
            onEndPosition = { state.eventSink(RouteScreen.Event.OnEndPosition(it)) },
            modifier = modifier,
        )
    }
}
