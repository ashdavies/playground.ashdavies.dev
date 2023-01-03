package io.ashdavies.playground

import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.slack.circuit.CircuitCompositionLocals
import com.slack.circuit.CircuitConfig
import com.slack.circuit.CircuitContent
import io.ashdavies.compose.ProvideFirebaseApp

private const val INTENT_EXTRA_ROUTE = "route"

internal class LauncherActivity : KotlinActivity({
    val route = enumValueOfOrNull<LauncherRoute>(intent.getStringExtra(INTENT_EXTRA_ROUTE))
    setDecorFitsSystemWindows(window, true)

    setContent {
        ProvideFirebaseApp {
            LauncherApp(
                circuitConfig = rememberCircuitConfig(defaultComponentContext()),
                route = route ?: LauncherRoute.Default
            )
        }
    }
})

@Composable
internal fun LauncherApp(
    circuitConfig: CircuitConfig,
    colorScheme: ColorScheme = dynamicColorScheme(isSystemInDarkTheme()),
    route: LauncherRoute = LauncherRoute.Default,
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = colorScheme.primaryContainer
    )

    MaterialTheme(colorScheme = colorScheme) {
        CircuitCompositionLocals(circuitConfig) {
            CircuitContent(AndroidLauncherScreen)
        }
    }
}

@Composable
private fun rememberCircuitConfig(componentContext: ComponentContext): CircuitConfig = remember {
    CircuitConfig.Builder()
        .addPresenterFactory(LauncherPresenterFactory())
        .addUiFactory(LauncherUiFactory(componentContext))
        .build()
}

private inline fun <reified T : Enum<T>> enumValueOfOrNull(value: String?): T? {
    val term = value?.lowercase() ?: return null
    return enumValues<T>().first {
        it.name.lowercase() == term
    }
}
