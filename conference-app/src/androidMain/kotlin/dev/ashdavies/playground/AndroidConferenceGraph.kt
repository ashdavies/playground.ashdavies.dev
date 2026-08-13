package dev.ashdavies.playground

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.FirebaseApp
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.firebase.FirebaseRemoteConfig
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.http.common.models.XAndroidCert
import dev.ashdavies.http.common.models.XAndroidPackage
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import io.ktor.http.HttpHeaders
import java.security.MessageDigest
import java.util.Locale

@DependencyGraph(AppScope::class)
internal interface AndroidConferenceGraph : ConferenceGraph {

    @Binds
    fun Activity.platformContext(): PlatformContext

    @Provides
    @Named("httpClientHeaders")
    fun httpClientHeaders(activity: Activity): Set<Pair<String, String>> = setOf(
        HttpHeaders.XAndroidCert to activity.getFirstSignatureOrThrow(),
        HttpHeaders.XAndroidPackage to activity.packageName,
        HttpHeaders.UserAgent to Build.PRODUCT,
    )

    @Provides
    fun remoteConfig(context: PlatformContext): RemoteConfig = FirebaseRemoteConfig(
        firebaseApp = requireNotNull(FirebaseApp.initializeApp(context)),
    )

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(@Provides activity: Activity): AndroidConferenceGraph
    }
}

private fun Context.getFirstSignatureOrThrow(): String {
    val signature = PackageInfoCompat
        .getSignatures(packageManager, packageName)
        .first()

    val digest = MessageDigest
        .getInstance("SHA1")
        .digest(signature.toByteArray())

    return digest.joinToString(separator = "") {
        String.format(Locale.getDefault(), "%02X", it)
    }
}
