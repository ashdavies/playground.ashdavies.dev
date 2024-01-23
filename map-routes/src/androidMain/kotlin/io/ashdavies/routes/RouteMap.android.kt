package io.ashdavies.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

public actual typealias LatLng = com.google.android.gms.maps.model.LatLng

@Composable
internal actual fun RouteMap(state: RouteMapState, modifier: Modifier) {
    GoogleMap(
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(state.startPosition, 12f)
        },
        modifier = modifier.fillMaxSize(),
        properties = MapProperties(
            isMyLocationEnabled = state.hasLocationPermission,
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = state.hasLocationPermission,
        ),
        onMyLocationButtonClick = {
            true
        },
    ) {
        Marker(
            state = MarkerState(state.startPosition),
            title = "Start",
        )
    }
}
