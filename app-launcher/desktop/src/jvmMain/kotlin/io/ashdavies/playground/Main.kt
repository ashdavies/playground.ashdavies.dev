package io.ashdavies.playground

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.slack.circuit.CircuitCompositionLocals
import com.slack.circuit.CircuitContent
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default

internal object DesktopLauncherScreen : LauncherScreen

public fun main(args: Array<String>) {
    val argParser = ArgParser("Playground")

    val launcherRoute by argParser
        .option(ArgType.Choice<LauncherRoute>(), "route")
        .default(LauncherRoute.Default)
        .apply { argParser.parse(args) }

    val circuitConfig = CircuitConfig(
        componentContext = DefaultComponentContext(DefaultLifecycle()),
        initialRoute = launcherRoute,
    )

    singleWindowApplication(
        state = WindowState(size = DpSize(450.dp, 975.dp)),
        title = argParser.programName,
    ) {
        MaterialTheme {
            CircuitCompositionLocals(circuitConfig) {
                CircuitContent(DesktopLauncherScreen)
            }
        }
    }
}
