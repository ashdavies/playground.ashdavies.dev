package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import io.ashdavies.routing.ComputeRoutesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Stable
internal data class RouteMapState(
    val startPosition: LatLng = KnownLocations.Berlin,
    val endPosition: LatLng? = null,
    val routes: List<ComputeRoutesResponse.Route> = emptyList(),
    val zoomLevel: Float = 12f,
)

@Composable
internal expect fun RouteMap(
    state: RouteMapState,
    onEndPosition: (LatLng) -> Unit,
    modifier: Modifier = Modifier,
)

internal data class LatLng(
    val latitude: Double,
    val longitude: Double,
) {
    init {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch { doSomething() }
    }

    private suspend fun doSomething() {
        println("Doing something")
    }
}
