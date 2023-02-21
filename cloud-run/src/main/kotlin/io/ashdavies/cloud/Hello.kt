package io.ashdavies.cloud

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

internal fun Route.hello() {
    get("/hello") {
        call.respond("Hello, World!")
    }
}
