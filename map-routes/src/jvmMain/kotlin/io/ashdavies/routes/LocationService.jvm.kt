package io.ashdavies.routes

import io.ashdavies.content.PlatformContext

internal actual fun LocationService(context: PlatformContext): LocationService {
    return LocationService { throw IllegalStateException("Not Supported") }
}
