package dev.ashdavies.playground

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.ashdavies.playground.material.padding
import dev.ashdavies.playground.material.spacing
import dev.zacsweers.metro.createGraphFactory

@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    ComposeViewport {
        ConferenceApp(PlatformContext)
    }
}

@Composable
private fun ConferenceApp(context: PlatformContext) {
    MaterialTheme {
        val conferenceGraph = remember(context) {
            val factory = createGraphFactory<WasmConferenceGraph.Factory>()
            factory.create(context)
        }

        CircuitCompositionLocals(conferenceGraph.circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(BottomBarScaffoldScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack) { },
                    backStack = backStack,
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Text(BuildConfig.VERSION_NAME)
        }
    }
}
