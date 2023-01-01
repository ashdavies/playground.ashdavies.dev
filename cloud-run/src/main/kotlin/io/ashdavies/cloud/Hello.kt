package io.ashdavies.cloud

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

internal fun Routing.hello() {
    get("/hello") {
        call.respond("Hello, World!")
    }
}
