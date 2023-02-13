package io.ashdavies.playground

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
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
            LauncherApp(
                initialBackStack = initialBackStack,
                circuitConfig = circuitConfig,
                onRootPop = ::exitApplication,
            )
        }
    }
}
