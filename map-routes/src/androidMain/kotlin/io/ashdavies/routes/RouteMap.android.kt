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
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.maps.model.LatLng as GmsLatLng

private const val CAMERA_ANIMATE_DURATION = 2_000

@Composable
internal actual fun RoutesMap(
    state: RoutesMapState,
    modifier: Modifier,
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(state.startPosition, state.zoomLevel) {
        val startPosition = state.startPosition.asGmsLatLng()
        val cameraPosition = CameraPosition.fromLatLngZoom(startPosition, state.zoomLevel)
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        cameraPositionState.animate(cameraUpdate, CAMERA_ANIMATE_DURATION)
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        modifier = modifier.fillMaxSize(),
        onMapClick = { onEndPosition(it.asLatLng()) },
    ) {
        Marker(
            state = rememberMarkerState(position = state.startPosition.asGmsLatLng()),
            icon = rememberGreenMarker(),
            title = "Start",
        )

        val endPosition = state.endPosition
        if (endPosition != null) {
            Marker(
                state = rememberMarkerState(position = endPosition.asGmsLatLng()),
                icon = rememberRedMarker(),
                title = "End",
            )
        }

        if (state.routes.isNotEmpty()) {
            Polyline(
                points = state.routes.flatMap {
                    PolyUtil.decode(it.polyline.encodedPolyline)
                },
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

private fun GmsLatLng.asLatLng() = LatLng(
    latitude = latitude,
    longitude = longitude,
)

private fun LatLng.asGmsLatLng() = GmsLatLng(
    /* latitude = */ latitude,
    /* longitude = */ longitude,
)
