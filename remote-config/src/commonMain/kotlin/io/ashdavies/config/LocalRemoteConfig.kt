package io.ashdavies.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public val LocalRemoteConfig: ProvidableCompositionLocal<RemoteConfig> = staticCompositionLocalOf {
    LazyRemoteConfig()
}
