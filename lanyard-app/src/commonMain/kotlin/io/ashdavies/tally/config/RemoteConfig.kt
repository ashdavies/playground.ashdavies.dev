package io.ashdavies.lanyard.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import io.ashdavies.config.LocalRemoteConfig
import io.ashdavies.config.RemoteConfig
import io.ashdavies.config.getBoolean

internal suspend fun RemoteConfig.isGalleryEnabled() = getBoolean("gallery_enabled")

@Composable
internal fun booleanConfigAsState(
    remoteConfig: RemoteConfig = LocalRemoteConfig.current,
    initialValue: Boolean = false,
    factory: suspend RemoteConfig.() -> Boolean,
): State<Boolean> = produceState(initialValue) {
    value = remoteConfig.factory()
}
