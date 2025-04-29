package io.ashdavies.routes

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.content.enableStrictMode
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache

@Composable
internal fun MapRoutesApp(context: Context = LocalContext.current) {
    val httpClient = remember { defaultHttpClient { installHttpCache(context) } }

    CircuitCompositionLocals(rememberCircuit(context, httpClient)) {
        ContentWithOverlays {
            val backStack = rememberSaveableBackStack(RoutesScreen)

            NavigableCircuitContent(
                navigator = rememberCircuitNavigator(backStack),
                backStack = backStack,
            )
        }
    }
}

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        enableStrictMode(false)

        setContent {
            MapRoutesApp()
        }
    }
}

private fun HttpClientConfig<*>.installHttpCache(context: Context) {
    install(HttpCache) { publicStorage(context.resolveCacheDir()) }
}
