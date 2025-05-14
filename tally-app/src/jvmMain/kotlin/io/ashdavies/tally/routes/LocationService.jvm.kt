package io.ashdavies.tally.routes

import io.ashdavies.content.PlatformContext
import io.ashdavies.tally.routes.LocationService

internal actual fun LocationService(context: PlatformContext): LocationService {
    return LocationService { throw IllegalStateException("Not Supported") }
}
