package io.ashdavies.analytics

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalAnalytics: ProvidableCompositionLocal<RemoteAnalytics> = staticCompositionLocalOf {
    RemoteAnalytics { name, parameters -> firebaseAnalytics.logEvent(name, parameters) }
}
