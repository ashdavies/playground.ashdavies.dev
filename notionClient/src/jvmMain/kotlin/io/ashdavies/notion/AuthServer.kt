package io.ashdavies.notion

import AuthSuccess
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.ApplicationRequest
import io.ktor.request.queryString
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED

private val ApplicationRequest.result: AuthResult
    get() {
        val code: String? = queryParameters["code"]
        val error: String? = queryParameters["error"]
        val state: String? = queryParameters["state"]

        return when {
            code != null -> AuthResult.Success(code, state)
            error != null -> AuthResult.Error(error, state)
            else -> throw IllegalStateException(
                "Unexpected server response ${queryString()}"
            )
        }
    }

private suspend fun ApplicationCall.respondHtml(text: String) = respondText(
    contentType = ContentType.Text.Html,
    status = HttpStatusCode.OK,
    text = text,
)

actual class AuthServer actual constructor(private val config: AuthServerConfig) {

    private val result = Channel<AuthResult>(CONFLATED)
    private val server = embeddedServer(CIO, config.port, config.host) {
        routing {
            get("/${config.path}") {
                result.send(call.request.result)
                call.respondHtml(AuthSuccess)
            }
        }
    }.start()

    actual suspend fun await(): AuthResult = result.receive()

    actual suspend fun close() {
        server.stop(500L, 500L)
        result.close()
    }
}
