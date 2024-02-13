package io.ashdavies.routes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun RouteMap(
    state: RouteMapState,
    modifier: Modifier,
    onEndPosition: (LatLng) -> Unit
) {
    Text("Unsupported Platform")
}
