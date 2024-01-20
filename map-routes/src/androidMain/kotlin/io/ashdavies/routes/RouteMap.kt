package io.ashdavies.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

private val Berlin = LatLng(52.5200, 13.4050)

@Composable
internal actual fun RouteMap(modifier: Modifier) {
    GoogleMap(
        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(Berlin, 10f)
        },
        modifier = modifier.fillMaxSize(),
    ) {
    }
}
