package io.ashdavies.cloud

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

internal fun Routing.token() {
    post("/token") {
        call.respond("Hello, World!")
    }
}
