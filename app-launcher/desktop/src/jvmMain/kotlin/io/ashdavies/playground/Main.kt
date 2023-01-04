package io.ashdavies.playground

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

internal object DesktopLauncherScreen : LauncherScreen

public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")
    val routeArgOption by argParser.option(ArgType.String, "route")
    val argResult = argParser.parse(args)

    val circuitConfig = CircuitConfig(DefaultComponentContext(DefaultLifecycle()))
    val initialBackStack = buildInitialBackStack(
        initialScreen = DesktopLauncherScreen,
        nextScreen = routeArgOption,
    )

    singleWindowApplication(
        content = { LauncherApp(circuitConfig, initialBackStack) },
        state = WindowState(size = DpSize(450.dp, 975.dp)),
        title = argResult.commandName,
    )
}
