package io.ashdavies.routes

import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberLocationPermissionState(): LocationPermissionState

internal interface LocationPermissionState {

    val allPermissionsGranted: Boolean
    val shouldShowRationale: Boolean

    fun launchMultiplePermissionRequest()
}
