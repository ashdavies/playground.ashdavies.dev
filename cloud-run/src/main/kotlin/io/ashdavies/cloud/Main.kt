package io.ashdavies.cloud

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.conditionalheaders.ConditionalHeaders
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.routing.routing

public fun main(args: Array<String>) {
    val server = embeddedServer(
        module = Application::main,
        factory = CIO,
        port = 8080,
    )

    server.start(wait = true)
}

private fun Application.main() {
    install(CallLogging)
    install(DefaultHeaders)

    install(Compression) {
        default()
    }

    install(ConditionalHeaders)

    routing {
        events()
        hello()
        token()
    }
}
