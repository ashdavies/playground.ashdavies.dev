package io.ashdavies.analytics

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

public val LocalAnalytics: ProvidableCompositionLocal<RemoteAnalytics> = staticCompositionLocalOf {
    RemoteAnalytics { name, parameters -> Firebase.analytics.logEvent(name, parameters) }
}
