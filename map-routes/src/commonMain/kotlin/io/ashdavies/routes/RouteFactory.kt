package io.ashdavies.routes

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ktor.client.HttpClient

internal fun Circuit.Builder.addRoutePresenter(
    httpClient: HttpClient,
    locationService: LocationService,
): Circuit.Builder = addPresenter<RouteScreen, RouteScreen.State> { _, _, _ ->
    presenterOf {
        RoutePresenter(
            locationService = locationService,
            httpClient = httpClient,
        )
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
