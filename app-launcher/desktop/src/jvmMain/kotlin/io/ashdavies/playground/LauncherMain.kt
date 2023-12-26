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
import io.ashdavies.http.HttpCredentials
import io.ashdavies.http.LocalHttpCredentials
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

private val ApiKey: String
    get() = System.getenv("PLAYGROUND_API_KEY")

private val UserAgent: String
    get() = System.getProperty("os.name")

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
                val credentials = remember { HttpCredentials(ApiKey, UserAgent) }

                CompositionLocalProvider(LocalHttpCredentials provides credentials) {
                    LauncherContent {
                        val backStack = rememberSaveableBackStack(routerArgOption)

                        NavigableCircuitContent(
                            navigator = rememberCircuitNavigator(backStack, ::exitApplication),
                            backstack = backStack,
                        )
                    }
                }
            }
        }
    }
}
