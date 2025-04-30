package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.foundation.Circuit
import io.ashdavies.content.PlatformContext
import io.ktor.client.HttpClient

@Composable
internal fun rememberCircuit(graph: RoutesGraph): Circuit = remember(graph) {
    Circuit.Builder()
        .addRoutesPresenter(graph.httpClient, graph.locationService)
        .addRoutesUi()
        .build()
}
