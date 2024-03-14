package io.ashdavies.routing

import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.UnaryCallable
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

private const val ROUTES_GOOGLE_APIS = "https://routes.googleapis.com"

private const val HEADER_API_KEY = "X-Goog-Api-Key"
private const val HEADER_FIELD_MASK = "X-Goog-FieldMask"

private const val FIELD_ENCODED_POLYLINE = "routes.distanceMeters,routes.duration,routes.polyline.encodedPolyline"

public class ComputeRoutesCallable(
    httpClient: HttpClient = defaultHttpClient(),
    apiKey: String,
) : UnaryCallable<ComputeRoutesRequest, ComputeRoutesResponse> {

    private val httpClient = httpClient.config {
        install(DefaultRequest) {
            header(HEADER_API_KEY, apiKey)
            header(HEADER_FIELD_MASK, FIELD_ENCODED_POLYLINE)

            url(ROUTES_GOOGLE_APIS)
        }

        install(HttpCallValidator) {
            handleResponseExceptionWithRequest { exception, _ ->
                if (exception is ClientRequestException) {
                    throw exception.response.body<ComputeRoutesError>()
                }
            }
        }

        expectSuccess = true
    }

    override suspend fun invoke(
        request: ComputeRoutesRequest,
    ): ComputeRoutesResponse = httpClient
        .post("/directions/v2:computeRoutes") { setBody(request) }
        .body()
}

@Serializable
public data class ComputeRoutesRequest(
    val origin: Origin,
    val destination: Destination,
    val travelMode: TravelMode,
    val departureTime: String,
    val languageCode: String,
    val units: Units,
) {

    public constructor(
        origin: LatLng,
        destination: LatLng,
        departureTime: String = "${Clock.System.now()}",
    ) : this(
        origin = Origin(Location(origin)),
        destination = Destination(Location(destination)),
        travelMode = TravelMode.WALK,
        departureTime = departureTime,
        languageCode = "en-GB",
        units = Units.METRIC,
    )

    @Serializable
    public data class Origin(
        val location: Location,
    )

    @Serializable
    public data class Destination(
        val location: Location,
    )

    @Serializable
    public data class Location(
        val latLng: LatLng,
    )

    @Serializable
    public data class LatLng(
        val latitude: Double,
        val longitude: Double,
    )

    @Serializable
    public enum class TravelMode {
        WALK,
    }

    @Serializable
    public enum class Units {
        METRIC,
    }
}

@Serializable
public data class ComputeRoutesResponse(
    val routes: List<Route>,
) {

    @Serializable
    public data class Route(
        val distanceMeters: Int,
        val duration: String,
        val polyline: Polyline,
    )

    @Serializable
    public data class Polyline(
        val encodedPolyline: String,
    )
}

@Serializable
public data class ComputeRoutesError(
    val error: Error,
) : Throwable() {

    override val message: String
        get() = error.message

    @Serializable
    public data class Error(
        val code: Int,
        val message: String,
        val status: String,
    )
}
