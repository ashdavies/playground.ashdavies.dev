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
import dev.zacsweers.metro.createGraphFactory
import io.ashdavies.content.enableStrictMode
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.tally.home.BottomBarScaffoldScreen

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

        val tallyGraph = remember(activity, windowSizeClass) {
            val factory = createGraphFactory<AndroidTallyGraph.Factory>()
            factory.create(activity, windowSizeClass)
        }

        CircuitCompositionLocals(tallyGraph.circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(BottomBarScaffoldScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack),
                    backStack = backStack,
                )
            }
        }
    }
}
