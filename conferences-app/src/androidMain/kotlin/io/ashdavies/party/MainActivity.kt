package io.ashdavies.party

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.core.content.pm.PackageInfoCompat
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.content.enableStrictMode
import io.ashdavies.content.isDebuggable
import io.ashdavies.http.ProvideHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.party.config.rememberCircuit
import io.ashdavies.party.home.HomeScreen
import io.ashdavies.party.material.ProvideLocalWindowSizeClass
import io.ashdavies.playground.BuildConfig
import io.ashdavies.playground.PlaygroundDatabase
import io.ashdavies.sql.ProvideTransacter
import io.ashdavies.sql.rememberTransacter
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header
import java.security.MessageDigest
import java.util.Locale

internal class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (BuildConfig.ANDROID_STRICT_MODE) {
            enableStrictMode(isDebuggable())
        }

        setContent {
            ConferencesApp(this)
        }
    }
}

@Composable
private fun ConferencesApp(activity: Activity) {
    ProvideHttpClient(
        config = {
            install(DefaultRequest) {
                header("X-Android-Cert", activity.getFirstSignatureOrNull())
                header("X-Android-Package", activity.packageName)
                header("X-API-Key", BuildConfig.ANDROID_API_KEY)
                header("User-Agent", Build.PRODUCT)
            }

            install(HttpCache) {
                publicStorage(activity.resolveCacheDir())
            }
        },
    ) {
        ProvideAppCheckToken {
            val transacter = rememberTransacter(
                schema = PlaygroundDatabase.Schema,
                context = activity,
            ) { PlaygroundDatabase(it) }

            ProvideTransacter(transacter) {
                MaterialTheme(dynamicColorScheme()) {
                    CircuitCompositionLocals(rememberCircuit(activity)) {
                        ContentWithOverlays {
                            @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
                            ProvideLocalWindowSizeClass(calculateWindowSizeClass(activity)) {
                                val backStack = rememberSaveableBackStack(HomeScreen)

                                NavigableCircuitContent(
                                    navigator = rememberCircuitNavigator(backStack),
                                    backStack = backStack,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Context.getFirstSignatureOrNull(): String? {
    val signature = PackageInfoCompat
        .getSignatures(packageManager, packageName)
        .firstOrNull() ?: return null

    val digest = MessageDigest
        .getInstance("SHA1")
        .digest(signature.toByteArray())

    return digest.joinToString(separator = "") {
        String.format(Locale.getDefault(), "%02X", it)
    }
}
