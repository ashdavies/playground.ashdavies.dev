package io.ashdavies.tally

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.tally.security.FirebaseAppCheckHeader
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header
import java.security.MessageDigest
import java.util.Locale

internal object AndroidTallyModule {

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

        install(HttpCache) {
            publicStorage(activity.resolveCacheDir())
        }
    }

    fun playgroundDatabase(context: PlatformContext): PlaygroundDatabase = DatabaseFactory(
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
