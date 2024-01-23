package io.ashdavies.routes

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@Composable
@OptIn(ExperimentalPermissionsApi::class)
internal actual fun rememberLocationPermissionState(): LocationPermissionState {
    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    )

    return remember(multiplePermissionState) {
        LocationPermissionState(
            allPermissionsGranted = multiplePermissionState.allPermissionsGranted,
            shouldShowRationale = multiplePermissionState.shouldShowRationale,
            launchMultiplePermissionRequest = multiplePermissionState::launchMultiplePermissionRequest,
        )
    }
}
