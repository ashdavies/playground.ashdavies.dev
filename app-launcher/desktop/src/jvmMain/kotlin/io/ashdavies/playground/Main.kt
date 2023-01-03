package io.ashdavies.playground

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.slack.circuit.CircuitCompositionLocals
import com.slack.circuit.CircuitConfig
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

    singleWindowApplication(
        state = WindowState(size = DpSize(450.dp, 975.dp)),
        title = argParser.programName,
    ) {
        val circuitConfig = rememberCircuitConfig(
            componentContext = DefaultComponentContext(DefaultLifecycle()),
            initialRoute = launcherRoute,
        )

        MaterialTheme {
            CircuitCompositionLocals(circuitConfig) {
                CircuitContent(DesktopLauncherScreen)
            }
        }
    }
}

@Composable
private fun rememberCircuitConfig(
    componentContext: ComponentContext,
    initialRoute: LauncherRoute,
): CircuitConfig = remember {
    CircuitConfig.Builder()
        .addPresenterFactory(LauncherPresenterFactory(initialRoute))
        .addUiFactory(LauncherUiFactory(componentContext))
        .build()
}
