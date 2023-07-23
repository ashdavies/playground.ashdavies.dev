package io.ashdavies.cloud

import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.Route

internal fun Route.android() {
    staticResources(
        remotePath = ".well-known/assetlinks.json",
        basePackage = "assetlinks.json",
    )
}
