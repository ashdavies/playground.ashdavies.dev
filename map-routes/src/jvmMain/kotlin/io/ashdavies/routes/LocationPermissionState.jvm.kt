package io.ashdavies.routes

import androidx.compose.runtime.Composable

@Composable
internal actual fun rememberLocationPermissionState(): LocationPermissionState {
    throw IllegalStateException("Unsupported Platform")
}
