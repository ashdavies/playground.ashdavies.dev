package dev.ashdavies.playground

import android.app.Activity
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.ashdavies.playground.material.dynamicColorScheme
import dev.zacsweers.metro.createGraphFactory

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
public fun ConferenceApp(activity: Activity) {
    MaterialExpressiveTheme(dynamicColorScheme()) {
        val conferenceGraph = remember(activity) {
            val factory = createGraphFactory<AndroidConferenceGraph.Factory>()
            factory.create(activity)
        }

        CircuitCompositionLocals(conferenceGraph.circuit) {
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
