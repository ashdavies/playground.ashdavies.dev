package dev.ashdavies.playground.routes

internal fun interface LocationService {
    suspend fun getLastLocation(): LatLng
}
