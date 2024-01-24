package io.ashdavies.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

public actual typealias LatLng = com.google.android.gms.maps.model.LatLng

private const val CAMERA_ANIMATE_DURATION = 2_000

@Composable
internal actual fun RouteMap(state: RouteMapState, modifier: Modifier) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(state.startPosition, state.zoomLevel) {
        val cameraPosition = CameraPosition.fromLatLngZoom(state.startPosition, state.zoomLevel)
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        cameraPositionState.animate(cameraUpdate, CAMERA_ANIMATE_DURATION)
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier.fillMaxSize(),
        onMapClick = { state.endPosition = it },
    ) {
        Marker(
            state = MarkerState(state.startPosition),
            icon = rememberGreenMarker(),
            title = "Start",
        )

        val endPosition = state.endPosition
        if (endPosition != null) {
            Marker(
                state = MarkerState(endPosition),
                icon = rememberRedMarker(),
                title = "End",
            )
        }
    }
}

@Composable
private fun rememberGreenMarker(): BitmapDescriptor = remember {
    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
}

@Composable
private fun rememberRedMarker(): BitmapDescriptor = remember {
    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
}
