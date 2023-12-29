package io.ashdavies.playground

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")
    val routerArgOption by argParser.option(ArgType.String, "route")
    val argResult = argParser.parse(args)

    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(450.dp, 975.dp)),
            title = argResult.commandName,
        ) {
            val circuit = remember { CircuitConfig(PlatformContext.Default) }

            CircuitCompositionLocals(circuit) {
                CompositionLocalProvider(
                    LocalHttpClient provides LocalHttpClient.current.config {
                        install(DefaultRequest) {
                            header("User-Agent", System.getProperty("os.name"))
                            header("X-API-Key", System.getenv("BROWSER_API_KEY"))
                        }
                    },
                ) {
                    LauncherContent {
                        val backStack = rememberSaveableBackStack(routerArgOption)

                        NavigableCircuitContent(
                            navigator = rememberCircuitNavigator(backStack, ::exitApplication),
                            backstack = backStack,
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
