package io.ashdavies.tally

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.ProvideHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.playground.KeyNavigationDecoration
import io.ashdavies.sql.ProvideTransacter
import io.ashdavies.sql.rememberTransacter
import io.ashdavies.tally.circuit.rememberCircuit
import io.ashdavies.tally.home.HomeScreen
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header

public fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Tally",
        ) {
            TallyApp(
                platformContext = PlatformContext.Default,
                onClose = ::exitApplication,
            )
        }
    }
}

@Composable
private fun TallyApp(
    platformContext: PlatformContext,
    onClose: () -> Unit,
) {
    ProvideHttpClient(
        config = {
            install(DefaultRequest) {
                header("User-Agent", System.getProperty("os.name"))
                header("X-API-Key", BuildConfig.BROWSER_API_KEY)
            }

            install(HttpCache) {
                publicStorage(platformContext.resolveCacheDir())
            }
        },
    ) {
        ProvideAppCheckToken {
            val transacter = rememberTransacter(
                schema = PlaygroundDatabase.Schema,
                context = platformContext,
            ) { PlaygroundDatabase(it) }

            ProvideTransacter(transacter) {
                MaterialTheme(dynamicColorScheme()) {
                    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
                    val circuit = rememberCircuit(
                        platformContext = platformContext,
                        windowSizeClass = calculateWindowSizeClass(),
                    )

                    CircuitCompositionLocals(circuit) {
                        ContentWithOverlays {
                            val backStack = rememberSaveableBackStack(HomeScreen)

                            NavigableCircuitContent(
                                navigator = rememberCircuitNavigator(backStack) { onClose() },
                                backStack = backStack,
                                decoration = KeyNavigationDecoration(
                                    decoration = circuit.defaultNavDecoration,
                                    onBackInvoked = backStack::pop,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
