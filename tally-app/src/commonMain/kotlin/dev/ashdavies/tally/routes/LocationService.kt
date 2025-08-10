package dev.ashdavies.tally.routes

internal fun interface LocationService {
    suspend fun getLastLocation(): LatLng
}
