package dev.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import dev.ashdavies.content.enableStrictMode
import dev.ashdavies.playground.home.BottomBarScaffoldScreen
import dev.zacsweers.metro.createGraphFactory

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        enableStrictMode(false)

        setContent {
            ConferenceApp(
                circuit = createGraphFactory<AndroidConferenceGraph.Factory>()
                    .create(this)
                    .circuit,
                navigator = rememberCircuitNavigator(
                    backStack = rememberSaveableBackStack(BottomBarScaffoldScreen),
                ),
                onClose = ::finish,
            )
        }
    }
}
