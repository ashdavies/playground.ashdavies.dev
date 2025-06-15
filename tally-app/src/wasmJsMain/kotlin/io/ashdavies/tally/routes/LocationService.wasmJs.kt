package io.ashdavies.tally.routes

import io.ashdavies.content.PlatformContext

internal actual fun LocationService(context: PlatformContext): LocationService = object : LocationService {
    override suspend fun getLastLocation() = KnownLocations.Berlin
}
