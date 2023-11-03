package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
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
import io.ashdavies.http.HttpCredentials
import io.ashdavies.http.LocalHttpCredentials
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")
    val routerArgOption by argParser.option(ArgType.String, "route")
    val argResult = argParser.parse(args)

    val initialBackStack = buildInitialBackStack(routerArgOption)
    val circuit = CircuitConfig(PlatformContext.Default)

    val credentials = HttpCredentials(
        apiKey = System.getProperty("PLAYGROUND_API_KEY"),
        userAgent = System.getProperty("os.name"),
    )

    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(size = DpSize(450.dp, 975.dp)),
            title = argResult.commandName,
        ) {
            val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }
            val navigator = rememberCircuitNavigator(backStack, ::exitApplication)

            MaterialTheme(dynamicColorScheme()) {
                CircuitCompositionLocals(circuit) {
                    CompositionLocalProvider(LocalHttpCredentials provides credentials) {
                        NavigableCircuitContent(navigator, backStack)
                    }
                }
            }
        }
    }
}
