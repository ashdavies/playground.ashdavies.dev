package io.ashdavies.playground

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

internal object DesktopLauncherScreen : LauncherScreen

@ExperimentalDecomposeApi
public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")
    val routeArgOption by argParser.option(ArgType.String, "route")

    val lifecycleRegistry = LifecycleRegistry()
    val argResult = argParser.parse(args)

    val circuitConfig = CircuitConfig(DefaultComponentContext(lifecycleRegistry))
    val initialBackStack = buildInitialBackStack(
        initialScreen = DesktopLauncherScreen,
        nextScreen = routeArgOption,
    )

    singleWindowApplication(
        state = WindowState(size = DpSize(450.dp, 975.dp)),
        title = argResult.commandName,
    ) {
        LifecycleController(
            lifecycleRegistry = lifecycleRegistry,
            windowState = rememberWindowState(),
        )

        LauncherApp(
            initialBackStack = initialBackStack,
            circuitConfig = circuitConfig,
        )
    }
}
