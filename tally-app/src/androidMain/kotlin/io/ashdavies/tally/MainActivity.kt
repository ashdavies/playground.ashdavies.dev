package io.ashdavies.tally

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.content.enableStrictMode
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.tally.circuit.CircuitModule
import io.ashdavies.tally.home.HomeScreen

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        enableStrictMode(false)

        setContent {
            TallyApp(this)
        }
    }
}

@Composable
private fun TallyApp(activity: Activity) {
    MaterialTheme(dynamicColorScheme()) {
        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        val windowSizeClass = calculateWindowSizeClass(activity)

        val circuit = remember(activity, windowSizeClass) {
            CircuitModule.circuit(
                playgroundDatabase = AndroidTallyModule.playgroundDatabase(activity),
                platformContext = activity,
                httpClient = AndroidTallyModule.httpClient(activity),
                windowSizeClass = windowSizeClass,
            )
        }

        CircuitCompositionLocals(circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(HomeScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack),
                    backStack = backStack,
                )
            }
        }
    }
}
