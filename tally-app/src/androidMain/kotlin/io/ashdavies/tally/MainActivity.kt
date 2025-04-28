package io.ashdavies.tally

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
import androidx.compose.runtime.remember
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import io.ashdavies.content.PlatformContext
import io.ashdavies.content.enableStrictMode
import io.ashdavies.content.isDebuggable
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.material.dynamicColorScheme
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.tally.circuit.rememberCircuit
import io.ashdavies.tally.home.HomeScreen
import io.ashdavies.tally.security.FirebaseAppCheckHeader
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
            TallyApp(this)
        }
    }
}

@Composable
private fun TallyApp(activity: Activity) {
    MaterialTheme(dynamicColorScheme()) {
        @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
        val circuit = rememberCircuit(
            playgroundDatabase = rememberPlaygroundDatabase(activity),
            platformContext = activity,
            httpClient = rememberHttpClient(activity),
            windowSizeClass = calculateWindowSizeClass(activity),
        )

        CircuitCompositionLocals(circuit) {
            ContentWithOverlays {
                val backStack = rememberSaveableBackStack(HomeScreen)

                NavigableCircuitContent(
                    navigator = rememberCircuitNavigator(backStack),
                    backStack = backStack,
                )
            }
        }
    }
}

@Composable
private fun rememberHttpClient(activity: Activity) = remember(activity) {
    defaultHttpClient {
        install(DefaultRequest) {
            header("X-Android-Cert", activity.getFirstSignatureOrNull())
            header("X-Android-Package", activity.packageName)
            header("X-API-Key", BuildConfig.ANDROID_API_KEY)
            header("User-Agent", Build.PRODUCT)
        }

        install(FirebaseAppCheckHeader) {
            val factory = PlayIntegrityAppCheckProviderFactory.getInstance()
            val appCheck = FirebaseAppCheck.getInstance()

            appCheck.installAppCheckProviderFactory(factory)
        }

        install(HttpCache) {
            publicStorage(activity.resolveCacheDir())
        }
    }
}

@Composable
private fun rememberPlaygroundDatabase(context: PlatformContext) = remember(context) {
    DatabaseFactory(
        schema = PlaygroundDatabase.Schema,
        context = context,
        factory = { PlaygroundDatabase(it) },
    )
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
