package io.ashdavies.routes

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.ashdavies.tally.routes.LocationPermissionState

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
        object : LocationPermissionState {
            override val allPermissionsGranted: Boolean =
                multiplePermissionState.allPermissionsGranted

            override val shouldShowRationale: Boolean =
                multiplePermissionState.shouldShowRationale

            override fun launchMultiplePermissionRequest() {
                multiplePermissionState.launchMultiplePermissionRequest()
            }
        }
    }
}
