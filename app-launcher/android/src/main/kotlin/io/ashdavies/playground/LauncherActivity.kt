package io.ashdavies.playground

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.http.HttpCredentials
import io.ashdavies.http.LocalHttpCredentials

internal class LauncherActivity : ComposeActivity(content = {
    val credentials = remember { HttpCredentials(BuildConfig.PLAYGROUND_API_KEY, Build.PRODUCT) }

    CompositionLocalProvider(LocalHttpCredentials provides credentials) {
        val circuit = remember { CircuitConfig(applicationContext) }

        CircuitCompositionLocals(circuit) {
            ContentWithOverlays {
                MaterialTheme(dynamicColorScheme()) {
                    val initialBackStack = remember {
                        buildInitialBackStack(intent.getStringExtra("route"))
                    }

                    val backStack = rememberSaveableBackStack {
                        initialBackStack.forEach(::push)
                    }

                    NavigableCircuitContent(
                        navigator = rememberCircuitNavigator(backStack),
                        backstack = backStack,
                    )
                }
            }
        }
    }
})
