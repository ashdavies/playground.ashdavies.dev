package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiState
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.routing.ComputeRoutesCallable
import io.ashdavies.routing.ComputeRoutesError
import io.ashdavies.routing.ComputeRoutesRequest
import io.ktor.client.HttpClient

@Composable
internal fun RoutePresenter(
    locationService: LocationService,
    httpClient: HttpClient = LocalHttpClient.current,
): RouteScreen.State {
    var startPosition by remember { mutableStateOf(KnownLocations.Berlin) }
    val locationPermissionState = rememberLocationPermissionState()

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        when (locationPermissionState.allPermissionsGranted) {
            false -> locationPermissionState.launchMultiplePermissionRequest()
            true -> startPosition = locationService.getLastLocation()
        }
    }

    val computeRoutes = remember { ComputeRoutesCallable(httpClient, BuildConfig.ANDROID_API_KEY) }
    var mapState by remember { mutableStateOf(RouteMapState(startPosition)) }
    var errorMessage = null as String?

    LaunchedEffect(mapState.endPosition) {
        val endPosition = mapState.endPosition ?: return@LaunchedEffect

        val computeRoutesRequest = ComputeRoutesRequest(
            origin = mapState.startPosition.asComputeRoutesRequestLatLng(),
            destination = endPosition.asComputeRoutesRequestLatLng(),
        )

        try {
            val computeRoutesResponse = computeRoutes(computeRoutesRequest)
            mapState = mapState.copy(routes = computeRoutesResponse.routes)
        } catch (exception: ComputeRoutesError) {
            errorMessage = exception.message
        }
    }

    return RouteScreen.State(
        mapState = mapState,
        errorMessage = errorMessage,
    ) { }
}

private fun LatLng.asComputeRoutesRequestLatLng(): ComputeRoutesRequest.LatLng {
    return ComputeRoutesRequest.LatLng(latitude, longitude)
}
