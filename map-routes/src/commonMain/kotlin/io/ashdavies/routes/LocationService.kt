package io.ashdavies.routes

import io.ashdavies.content.PlatformContext

internal fun interface LocationService {
    suspend fun getLastLocation(): LatLng
}

internal expect fun LocationService(
    context: PlatformContext,
): LocationService
