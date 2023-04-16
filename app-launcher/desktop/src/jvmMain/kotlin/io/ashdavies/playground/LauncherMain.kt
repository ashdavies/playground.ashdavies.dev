package io.ashdavies.playground

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.push
import com.slack.circuit.foundation.rememberCircuitNavigator
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")
    val routerArgOption by argParser.option(ArgType.String, "route")
    val argResult = argParser.parse(args)

    val initialBackStack = buildInitialBackStack(routerArgOption)
    val circuitConfig = CircuitConfig()

    application {
        val windowState = rememberWindowState(size = DpSize(450.dp, 975.dp))

        Window(
            onCloseRequest = ::exitApplication,
            title = argResult.commandName,
            state = windowState,
        ) {
            val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }
            val navigator = rememberCircuitNavigator(backStack, ::exitApplication)
            val colorScheme = dynamicColorScheme()

            MaterialTheme(colorScheme = colorScheme) {
                CircuitCompositionLocals(circuitConfig) {
                    NavigableCircuitContent(navigator, backStack)
                }
            }
        }
    }
}
