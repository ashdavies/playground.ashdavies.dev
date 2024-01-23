package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
internal data class RouteMapState(
    val startPosition: LatLng = KnownLocations.Berlin,
    val hasLocationPermission: Boolean = false,
)

@Composable
internal expect fun RouteMap(
    state: RouteMapState,
    modifier: Modifier = Modifier,
)

public expect class LatLng(
    latitude: Double,
    longitude: Double,
)
