package io.ashdavies.routes

import androidx.compose.runtime.Composable
import io.ashdavies.tally.routes.LocationPermissionState

@Composable
internal actual fun rememberLocationPermissionState(): LocationPermissionState {
    return object : LocationPermissionState {
        override val allPermissionsGranted = false
        override val shouldShowRationale = false

        override fun launchMultiplePermissionRequest() = Unit
    }
}
