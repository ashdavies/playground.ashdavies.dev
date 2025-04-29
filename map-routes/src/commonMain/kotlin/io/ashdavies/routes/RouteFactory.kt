package io.ashdavies.routes

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.presenterOf
import io.ktor.client.HttpClient

internal fun Circuit.Builder.addRoutesPresenter(
    httpClient: HttpClient,
    locationService: LocationService,
): Circuit.Builder = addPresenter<RoutesScreen, RoutesScreen.State> { _, _, _ ->
    presenterOf {
        RoutesPresenter(
            locationService = locationService,
            httpClient = httpClient,
        )
    }
}

public fun Circuit.Builder.addRoutesUi(): Circuit.Builder {
    return addUi<RoutesScreen, RoutesScreen.State> { state, modifier ->
        RoutesScreen(
            state = state,
            onEndPosition = { state.eventSink(RoutesScreen.Event.OnEndPosition(it)) },
            modifier = modifier,
        )
    }
}
