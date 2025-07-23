package io.ashdavies.tally.routes

internal fun interface LocationService {
    suspend fun getLastLocation(): LatLng
}
