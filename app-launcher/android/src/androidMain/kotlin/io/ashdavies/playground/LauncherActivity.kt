package io.ashdavies.playground

import android.os.Build
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
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
                LauncherContent {
                    val backStack = rememberSaveableBackStack(intent.getStringExtra("route"))

                    NavigableCircuitContent(
                        navigator = rememberCircuitNavigator(backStack),
                        backstack = backStack,
                    )
                }
            }
        }
    }
})
