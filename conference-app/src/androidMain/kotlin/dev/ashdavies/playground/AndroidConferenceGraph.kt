package dev.ashdavies.playground

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.FirebaseApp
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.firebase.FirebaseRemoteConfig
import dev.ashdavies.content.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import java.security.MessageDigest
import java.util.Locale

@DependencyGraph(AppScope::class)
internal interface AndroidConferenceGraph : ConferenceGraph {

    @Binds
    fun Activity.platformContext(): PlatformContext

    @Provides
    @Named("httpClientHeaders")
    fun httpClientHeaders(activity: Activity): Set<Pair<String, String>> = setOf(
        "X-Android-Cert" to activity.getFirstSignatureOrThrow(),
        "X-Android-Package" to activity.packageName,
        "User-Agent" to Build.PRODUCT,
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
