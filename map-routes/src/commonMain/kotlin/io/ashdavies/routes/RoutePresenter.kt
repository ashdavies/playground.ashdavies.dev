package io.ashdavies.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import io.ashdavies.delegates.notNull
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.routing.ComputeRoutesCallable
import io.ashdavies.routing.ComputeRoutesError
import io.ashdavies.routing.ComputeRoutesRequest
import io.ktor.client.HttpClient

private const val ROUTES_BASE_URL = "https://routes.googleapis.com"

@Composable
internal fun RoutePresenter(
    locationService: LocationService,
    httpClient: HttpClient = LocalHttpClient.current,
): RouteScreen.State {
    var startPosition by rememberRetained { mutableStateOf(KnownLocations.Berlin) }
    val locationPermissionState = rememberLocationPermissionState()

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        when (locationPermissionState.allPermissionsGranted) {
            false -> locationPermissionState.launchMultiplePermissionRequest()
            true -> startPosition = locationService.getLastLocation()
        }
    }

    val androidApiKey by notNull { BuildConfig.ANDROID_API_KEY }

    val computeRoutes = rememberRetained {
        ComputeRoutesCallable(
            httpClient = httpClient,
            baseUrl = ROUTES_BASE_URL,
            apiKey = androidApiKey,
        )
    }

    var mapState by rememberRetained { mutableStateOf(RouteMapState(startPosition)) }
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
