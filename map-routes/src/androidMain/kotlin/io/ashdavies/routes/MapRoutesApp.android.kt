package io.ashdavies.routes

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.content.enableStrictMode
import io.ashdavies.content.isDebuggable
import io.ashdavies.http.ProvideHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.cache.HttpCache

@Composable
internal fun MapRoutesApp(context: Context = LocalContext.current) {
    ProvideHttpClient(config = { installHttpCache(context) }) {
        CircuitCompositionLocals(rememberCircuit(context)) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(RouteScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack),
                    backStack = backStack,
                )
            }
        }
    }
}

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (BuildConfig.ANDROID_STRICT_MODE) {
            enableStrictMode(isDebuggable())
        }

        setContent {
            MapRoutesApp()
        }
    }
}

private fun HttpClientConfig<*>.installHttpCache(context: Context) {
    install(HttpCache) { publicStorage(context.resolveCacheDir()) }
}
