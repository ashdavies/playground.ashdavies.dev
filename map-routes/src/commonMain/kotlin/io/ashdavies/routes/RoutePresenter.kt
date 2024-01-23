package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiState

@Composable
internal fun RoutePresenter(locationService: LocationService): CircuitUiState {
    var startPosition by remember { mutableStateOf(KnownLocations.Berlin) }
    val locationPermissionState = rememberLocationPermissionState()

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        when (locationPermissionState.allPermissionsGranted) {
            false -> locationPermissionState.launchMultiplePermissionRequest()
            true -> startPosition = locationService.getLastLocation()
        }
    }

    return RouteScreen.State(
        mapState = RouteMapState(
            startPosition = startPosition,
            hasLocationPermission = locationPermissionState.allPermissionsGranted,
        ),
    ) { }
}
