package io.ashdavies.cloud

import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.Route

internal fun Route.static() {
    staticResources(
        remotePath = "/.well-known/",
        basePackage = "well-known",
    )
}
