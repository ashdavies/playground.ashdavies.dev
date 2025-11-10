package dev.ashdavies.playground.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.getBoolean

internal suspend fun RemoteConfig.isGalleryEnabled() = getBoolean("gallery_enabled")

internal suspend fun RemoteConfig.isPastEventsEnabled() = getBoolean("past_events_enabled")

internal suspend fun RemoteConfig.isRoutesEnabled() = getBoolean("routes_enabled")

@Composable
internal fun RemoteConfig.booleanConfigAsState(
    initialValue: Boolean = false,
    factory: suspend RemoteConfig.() -> Boolean,
): State<Boolean> = produceState(initialValue, factory) {
    value = factory()
}
