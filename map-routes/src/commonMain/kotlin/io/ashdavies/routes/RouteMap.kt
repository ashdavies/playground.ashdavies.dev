package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.ashdavies.routing.ComputeRoutesResponse

@Stable
internal data class RouteMapState(
    val startPosition: LatLng = KnownLocations.Berlin,
    val routes: List<ComputeRoutesResponse.Route> = emptyList(),
    val zoomLevel: Float = 12f,
) {

    var endPosition by mutableStateOf<LatLng?>(null)
}

@Composable
internal expect fun RouteMap(
    state: RouteMapState,
    modifier: Modifier = Modifier,
)

public expect class LatLng(
    latitude: Double,
    longitude: Double,
) {
    public val latitude: Double
    public val longitude: Double
}
