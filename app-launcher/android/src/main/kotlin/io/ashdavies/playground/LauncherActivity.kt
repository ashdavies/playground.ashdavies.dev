package io.ashdavies.playground

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.compose.ProvideFirebaseApp

internal class LauncherActivity : KotlinActivity(action = {
    val initialBackStack = buildInitialBackStack(intent.getStringExtra("route"))
    val circuitConfig = CircuitConfig(applicationContext)

    setContent {
        val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }
        val navigator = rememberCircuitNavigator(backStack)

        ProvideFirebaseApp {
            MaterialTheme(colorScheme = dynamicColorScheme()) {
                CircuitCompositionLocals(circuitConfig) {
                    ContentWithOverlays { NavigableCircuitContent(navigator, backStack) }
                }
            }
        }
    }
})
