package io.ashdavies.playground

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")

    val launcherRoute by argParser
        .option(ArgType.Choice<LauncherRoute>(), "route")
        .default(LauncherRoute.Default)
        .apply { argParser.parse(args) }

    singleWindowApplication(
        content = { LauncherScreen(DefaultComponentContext(DefaultLifecycle()), launcherRoute) },
        state = WindowState(size = DpSize(450.dp, 975.dp)),
        title = argParser.programName,
    )
}
