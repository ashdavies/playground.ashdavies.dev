package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ktor.client.HttpClient

@Composable
public fun rememberCircuit(
    platformContext: PlatformContext,
    httpClient: HttpClient,
): Circuit = remember(platformContext) {
    val locationService = LocationService(platformContext)

    Circuit.Builder()
        .addRoutesPresenter(httpClient, locationService)
        .addRoutesUi()
        .build()
}
