package io.ashdavies.playground

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.content.enableStrictMode
import io.ashdavies.content.isDebuggable
import io.ashdavies.http.ProvideHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header
import java.security.MessageDigest
import java.util.Locale

@Composable
private fun LauncherApp(context: Context = LocalContext.current, extra: (String) -> String?) {
    ProvideHttpClient(
        config = {
            install(DefaultRequest) {
                header("X-Android-Cert", getSignature(context.packageManager, context.packageName))
                header("X-Android-Package", context.packageName)
                header("X-API-Key", BuildConfig.ANDROID_API_KEY)
                header("User-Agent", Build.PRODUCT)
            }

            install(HttpCache) {
                publicStorage(context.resolveCacheDir())
            }
        },
    ) {
        CircuitCompositionLocals(remember { Circuit(context) }) {
            ContentWithOverlays {
                LauncherContent(context) {
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

internal class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (BuildConfig.ANDROID_STRICT_MODE) {
            enableStrictMode(isDebuggable())
        }

        setContent {
            LauncherApp { intent.getStringExtra(it) }
        }
    }
}

private fun getSignature(packageManager: PackageManager, packageName: String): String {
    val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
    val signature = packageInfo.signatures[0].toByteArray()
    val digest = MessageDigest.getInstance("SHA1").digest(signature)

    return digest.joinToString(separator = "") {
        String.format(Locale.getDefault(), "%02X", it)
    }
}
