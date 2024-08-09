package io.ashdavies.party.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import io.ashdavies.config.RemoteConfig
import io.ashdavies.config.getBoolean

internal suspend fun RemoteConfig.isProfileEnabled() = getBoolean("profile_enabled")

internal suspend fun RemoteConfig.isHomeEnabled() = getBoolean("home_enabled")

@Composable
internal fun booleanConfigAsState(
    remoteConfig: RemoteConfig = RemoteConfig,
    initialValue: Boolean = false,
    factory: suspend RemoteConfig.() -> Boolean,
): State<Boolean> = produceState(initialValue) {
    value = remoteConfig.factory()
}
