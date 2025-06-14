package io.ashdavies.tally.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberLocationPermissionState(): LocationPermissionState = remember {
    StubLocationPermissionState()
}

private class StubLocationPermissionState : LocationPermissionState {

    override val allPermissionsGranted = false
    override val shouldShowRationale = false

    override fun launchMultiplePermissionRequest() = Unit
}

