package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.playground.KeyNavigationDecoration
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header

@Composable
internal fun MapRoutesApp(onClose: () -> Unit) {
    val httpClient = remember { defaultHttpClient { installUserAgent() } }
    val circuit = rememberCircuit(PlatformContext.Default, httpClient)

    CircuitCompositionLocals(circuit) {
        val backStack = rememberSaveableBackStack(RouteScreen)

        NavigableCircuitContent(
            navigator = rememberCircuitNavigator(backStack) { onClose() },
            backStack = backStack,
            decoration = KeyNavigationDecoration(
                decoration = circuit.defaultNavDecoration,
                onBackInvoked = backStack::pop,
            ),
        )
    }
}

public fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(450.dp, 975.dp)),
            title = "MapRoutes",
            content = { MapRoutesApp(::exitApplication) },
        )
    }
}

private fun HttpClientConfig<*>.installUserAgent() {
    install(DefaultRequest) {
        header("User-Agent", System.getProperty("os.name"))
        // header("X-API-Key", BuildConfig.BROWSER_API_KEY)
    }
}
