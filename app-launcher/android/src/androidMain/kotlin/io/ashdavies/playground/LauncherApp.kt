package io.ashdavies.playground

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.http.ProvideHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ktor.client.plugins.cache.HttpCache

@Composable
internal fun LauncherApp(context: Context = LocalContext.current, extra: (String) -> String?) {
    ProvideHttpClient(
        config = {
            install(HttpCache) {
                publicStorage(context.resolveCacheDir())
            }
        },
    ) {
        LauncherContent(context) {
            CircuitCompositionLocals(rememberCircuit(context)) {
                ContentWithOverlays {
                    val backStack = rememberSaveableBackStack(extra("route"))

                    NavigableCircuitContent(
                        navigator = rememberCircuitNavigator(backStack),
                        backStack = backStack,
                    )
                }
            }
        }
    }
}
