package io.ashdavies.playground

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.content.enableStrictMode
import io.ashdavies.content.isDebuggable
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import java.security.MessageDigest
import java.util.Locale

@Composable
private fun Activity.LauncherApp() {
    CompositionLocalProvider(
        LocalHttpClient provides LocalHttpClient.current.config {
            install(DefaultRequest) {
                header("X-Android-Cert", getSignature(packageManager, packageName))
                header("X-Android-Package", packageName)
                header("X-API-Key", BuildConfig.ANDROID_API_KEY)
                header("User-Agent", Build.PRODUCT)
            }
        },
    ) {
        CircuitCompositionLocals(remember { CircuitConfig(applicationContext) }) {
            ContentWithOverlays {
                LauncherContent(LocalContext.current) {
                    val backStack = rememberSaveableBackStack(intent.getStringExtra("route"))

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
        enableStrictMode(isDebuggable())
        setContent { LauncherApp() }
    }
}

@Suppress("DEPRECATION")
private fun getSignature(packageManager: PackageManager, packageName: String): String {
    val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
    val signature = packageInfo.signatures[0].toByteArray()
    val digest = MessageDigest.getInstance("SHA1").digest(signature)

    return digest.joinToString(separator = "") {
        String.format(Locale.getDefault(), "%02X", it)
    }
}
