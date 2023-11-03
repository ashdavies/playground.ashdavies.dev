package io.ashdavies.playground

import android.os.Build
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.http.HttpCredentials
import io.ashdavies.http.LocalHttpCredentials

internal class LauncherActivity : KotlinActivity(action = {
    val credentials = HttpCredentials(getString(R.string.playground_api_key), Build.PRODUCT)
    val initialBackStack = buildInitialBackStack(intent.getStringExtra("route"))
    val circuit = CircuitConfig(applicationContext)

    setContent {
        val backStack = rememberSaveableBackStack { initialBackStack.forEach(::push) }

        CompositionLocalProvider(LocalHttpCredentials provides credentials) {
            CircuitCompositionLocals(circuit) {
                ContentWithOverlays {
                    MaterialTheme(dynamicColorScheme()) {
                        NavigableCircuitContent(
                            navigator = rememberCircuitNavigator(backStack),
                            backstack = backStack,
                        )
                    }
                }
            }
        }
    }
})
