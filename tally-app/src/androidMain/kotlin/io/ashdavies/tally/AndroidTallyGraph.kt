package io.ashdavies.tally

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import dev.ashdavies.http.defaultHttpClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ashdavies.content.PlatformContext
import io.ashdavies.tally.security.FirebaseAppCheckHeader
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import java.security.MessageDigest
import java.util.Locale

@DependencyGraph(AppScope::class)
internal interface AndroidTallyGraph : TallyGraph {

    @Binds
    fun Activity.platformContext(): PlatformContext

    @Provides
    fun httpClient(activity: Activity): HttpClient = defaultHttpClient {
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
    }

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(
            @Provides activity: Activity,
            @Provides windowSizeClass: WindowSizeClass,
        ): AndroidTallyGraph
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
