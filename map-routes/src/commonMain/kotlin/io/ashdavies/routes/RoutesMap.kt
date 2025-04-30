package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import io.ashdavies.routing.ComputeRoutesResponse

@Stable
internal data class RoutesMapState(
    val startPosition: LatLng,
    val endPosition: LatLng?,
    val routes: List<ComputeRoutesResponse.Route>,
    val zoomLevel: Float,
)

@Composable
internal expect fun RoutesMap(
    state: RoutesMapState,
    onEndPosition: (LatLng) -> Unit,
    modifier: Modifier = Modifier,
)

internal data class LatLng(
    val latitude: Double,
    val longitude: Double,
)
