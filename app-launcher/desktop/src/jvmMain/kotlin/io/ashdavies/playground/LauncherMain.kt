package io.ashdavies.playground

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

@ExperimentalDecomposeApi
public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")
    val routerArgOption by argParser.option(ArgType.String, "route")

    val lifecycleRegistry = LifecycleRegistry()
    val argResult = argParser.parse(args)

    val circuitConfig = CircuitConfig(DefaultComponentContext(lifecycleRegistry))
    val initialBackStack = buildInitialBackStack(routerArgOption)

    application {
        val windowState = rememberWindowState(size = DpSize(450.dp, 975.dp))

        Window(
            onCloseRequest = ::exitApplication,
            title = argResult.commandName,
            state = windowState,
        ) {
            LifecycleController(
                lifecycleRegistry = lifecycleRegistry,
                windowState = windowState,
            )

            LauncherApp(
                initialBackStack = initialBackStack,
                circuitConfig = circuitConfig,
                onRootPop = ::exitApplication,
            )
        }
    }
}
