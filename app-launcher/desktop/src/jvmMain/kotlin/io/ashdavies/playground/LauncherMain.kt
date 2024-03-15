package io.ashdavies.playground

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header

private class LauncherCommand : CliktCommand() {

    val route: String? by option(help = "The initial route to navigate to")

    override fun run() = application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(450.dp, 975.dp)),
            title = commandName,
        ) {
            val circuit = remember { Circuit(PlatformContext.Default) }

            CircuitCompositionLocals(circuit) {
                CompositionLocalProvider(
                    LocalHttpClient provides LocalHttpClient.current.config {
                        install(DefaultRequest) {
                            header("User-Agent", System.getProperty("os.name"))
                            header("X-API-Key", BuildConfig.BROWSER_API_KEY)
                        }
                    },
                ) {
                    LauncherContent(PlatformContext.Default) {
                        val backStack = rememberSaveableBackStack(route)

                        NavigableCircuitContent(
                            navigator = rememberCircuitNavigator(backStack, ::exitApplication),
                            backStack = backStack,
                            decoration = KeyNavigationDecoration(
                                decoration = circuit.defaultNavDecoration,
                                onBackInvoked = backStack::pop,
                            ),
                        )
                    }
                }
            }
        }
    }
}

public fun main(args: Array<String>) {
    LauncherCommand().main(args)
}
