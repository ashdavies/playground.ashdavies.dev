package io.ashdavies.routes

import androidx.compose.runtime.Composable

@Composable
internal actual fun rememberLocationPermissionState(): LocationPermissionState {
    return object : LocationPermissionState {
        override val allPermissionsGranted = false
        override val shouldShowRationale = false

        override fun launchMultiplePermissionRequest() = Unit
    }
}
