package io.ashdavies.playground

import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.push
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.compose.ProvideFirebaseApp

internal class LauncherActivity : KotlinActivity({
    val initialBackStack = buildInitialBackStack(intent.getStringExtra("route"))
    val circuitConfig = CircuitConfig(applicationContext)

    setContent {
        val systemUiController =
            rememberSystemUiController()
        val colorScheme = dynamicColorScheme()

        val primaryContainer = colorScheme.primaryContainer
        systemUiController.setSystemBarsColor(primaryContainer)

        val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }
        val navigator = rememberCircuitNavigator(backStack)

        ProvideFirebaseApp {
            MaterialTheme(colorScheme = colorScheme) {
                CircuitCompositionLocals(circuitConfig) {
                    ContentWithOverlays { NavigableCircuitContent(navigator, backStack) }
                }
            }
        }
    }
},)
