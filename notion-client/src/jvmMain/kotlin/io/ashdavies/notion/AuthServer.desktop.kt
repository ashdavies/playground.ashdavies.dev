package io.ashdavies.notion

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

internal actual suspend fun awaitAuthorizationCode(): String = suspendCancellableCoroutine { continuation ->
    val applicationEngine = embeddedServer(CIO, port = 8080) {
        routing {
            get("/callback") {
                continuation.resume(requireNotNull(call.parameters["code"]))
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }

    continuation.invokeOnCancellation {
        applicationEngine.stop()
    }

    applicationEngine.start()
}


internal actual fun getRedirectUrlString(): String {
    return "http://localhost:8080/callback"
}
