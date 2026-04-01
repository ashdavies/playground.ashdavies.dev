package dev.ashdavies.playground

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dev.ashdavies.material.dynamicColorScheme
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.zacsweers.metro.createGraphFactory

@Composable
public fun ConferenceApp(activity: Activity) {
    MaterialTheme(dynamicColorScheme()) {
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
