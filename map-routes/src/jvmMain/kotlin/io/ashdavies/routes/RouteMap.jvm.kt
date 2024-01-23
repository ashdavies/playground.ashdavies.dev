package io.ashdavies.routes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

public actual class LatLng actual constructor(latitude: Double, longitude: Double)

@Composable
internal actual fun RouteMap(state: RouteMapState, modifier: Modifier) {
    Text("Unsupported Platform")
}
