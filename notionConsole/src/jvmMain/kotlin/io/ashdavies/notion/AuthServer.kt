package io.ashdavies.notion

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.uri
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.CompletableDeferred

private suspend fun ApplicationCall.respondHtml(text: String) = respondText(
    contentType = ContentType.Text.Html,
    status = HttpStatusCode.OK,
    text = text,
)

actual class AuthServer actual constructor(host: String, path: String, port: Int) {

    private val redirectUri = CompletableDeferred<String>()
    private val server = embeddedServer(CIO, port, host) {
        routing {
            get("/$path") {
                redirectUri.complete(call.request.uri)
                call.respondHtml(AuthSuccess)
                stop()
            }
        }
    }.start()

    actual suspend fun awaitRedirectUri(): String = redirectUri.await()

    private fun stop() {
        server.stop(500L, 500L)
    }
}
