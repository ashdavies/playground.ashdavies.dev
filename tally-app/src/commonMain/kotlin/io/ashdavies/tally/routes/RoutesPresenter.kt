package io.ashdavies.tally.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.presenter.Presenter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ashdavies.delegates.notNull
import io.ashdavies.routing.ComputeRoutesCallable
import io.ashdavies.routing.ComputeRoutesError
import io.ashdavies.routing.ComputeRoutesRequest
import io.ashdavies.tally.BuildConfig
import io.ashdavies.tally.circuit.CircuitScreenKey
import io.ktor.client.HttpClient

private const val ROUTES_BASE_URL = "https://routes.googleapis.com"

private val InitialRoutesMapState = RoutesMapState(
    startPosition = KnownLocations.Berlin,
    endPosition = null,
    routes = emptyList(),
    zoomLevel = 12f,
)

@CircuitScreenKey(RoutesScreen::class)
@ContributesIntoMap(AppScope::class, binding<Presenter<*>>())
internal class RoutesPresenter @Inject constructor(
    private val locationService: LocationService,
    private val httpClient: HttpClient,
) : Presenter<RoutesScreen.State> {

    @Composable
    override fun present(): RoutesScreen.State {
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

        var mapState by rememberRetained {
            mutableStateOf(InitialRoutesMapState.copy(startPosition = startPosition))
        }

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

        return RoutesScreen.State(
            mapState = mapState,
            errorMessage = errorMessage,
        ) { }
    }
}

private fun LatLng.asComputeRoutesRequestLatLng(): ComputeRoutesRequest.LatLng {
    return ComputeRoutesRequest.LatLng(latitude, longitude)
}
