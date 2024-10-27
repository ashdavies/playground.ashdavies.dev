package io.ashdavies.party

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.analytics.LocalAnalytics
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.config.LocalConfigValue
import io.ashdavies.config.LocalRemoteConfig
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.ProvideHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.party.config.rememberCircuit
import io.ashdavies.party.firebase.EmptyLocalConfigValue
import io.ashdavies.party.home.HomeScreen
import io.ashdavies.party.material.ProvideLocalWindowSizeClass
import io.ashdavies.playground.BuildConfig
import io.ashdavies.playground.KeyNavigationDecoration
import io.ashdavies.playground.PlaygroundDatabase
import io.ashdavies.sql.ProvideTransacter
import io.ashdavies.sql.rememberTransacter
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header

public fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Conferences",
        ) {
            ConferencesApp(
                context = PlatformContext.Default,
                onClose = ::exitApplication,
            )
        }
    }
}

@Composable
private fun ConferencesApp(
    context: PlatformContext,
    onClose: () -> Unit,
) {
    ProvideHttpClient(
        config = {
            install(DefaultRequest) {
                header("User-Agent", System.getProperty("os.name"))
                header("X-API-Key", BuildConfig.BROWSER_API_KEY)
            }

            install(HttpCache) {
                publicStorage(context.resolveCacheDir())
            }
        },
    ) {
        ProvideRemoteLocals {
            ProvideAppCheckToken {
                val transacter = rememberTransacter(
                    schema = PlaygroundDatabase.Schema,
                    context = context,
                ) { PlaygroundDatabase(it) }

                ProvideTransacter(transacter) {
                    MaterialTheme(dynamicColorScheme()) {
                        val circuit = rememberCircuit(context)

                        CircuitCompositionLocals(circuit) {
                            ContentWithOverlays {
                                @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
                                ProvideLocalWindowSizeClass(calculateWindowSizeClass()) {
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
    }
}

@Composable
private fun ProvideRemoteLocals(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAnalytics provides RemoteAnalytics { _, _ -> },
        LocalRemoteConfig provides object : RemoteConfig {
            override suspend fun <T : Any> getValue(
                key: String,
                transform: (LocalConfigValue) -> T,
            ): T = transform(EmptyLocalConfigValue)
        },
        content = content,
    )
}
