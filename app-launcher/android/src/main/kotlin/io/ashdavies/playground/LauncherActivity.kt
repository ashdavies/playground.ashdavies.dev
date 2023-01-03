package io.ashdavies.playground

import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
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
import kotlinx.parcelize.Parcelize

private const val INTENT_EXTRA_ROUTE = "route"

@Parcelize
internal object AndroidLauncherScreen : LauncherScreen, Parcelable

internal class LauncherActivity : KotlinActivity({
    val initialRoute = enumValueOfOrDefault(intent.getStringExtra(INTENT_EXTRA_ROUTE)) {
        LauncherRoute.Default
    }

    setDecorFitsSystemWindows(window, true)

    setContent {
        ProvideFirebaseApp {
            LauncherApp(rememberCircuitConfig(defaultComponentContext(), initialRoute))
        }
    }
})

@Composable
internal fun LauncherApp(circuitConfig: CircuitConfig) {
    val colorScheme = dynamicColorScheme(isSystemInDarkTheme())
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
private fun rememberCircuitConfig(
    componentContext: ComponentContext,
    initialRoute: LauncherRoute,
): CircuitConfig = remember {
    CircuitConfig.Builder()
        .addPresenterFactory(LauncherPresenterFactory(initialRoute))
        .addUiFactory(LauncherUiFactory(componentContext))
        .build()
}

private inline fun <reified T : Enum<T>> enumValueOfOrDefault(
    value: String?,
    otherwise: () -> T,
): T = value?.lowercase()?.let { term ->
    enumValues<T>().firstOrNull {
        it.name.lowercase() == term
    }
} ?: otherwise()
