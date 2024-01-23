package io.ashdavies.routes

import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberLocationPermissionState(): LocationPermissionState

internal data class LocationPermissionState(
    val allPermissionsGranted: Boolean,
    val shouldShowRationale: Boolean,
    val launchMultiplePermissionRequest: () -> Unit,
)
