package io.ashdavies.notion

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.uri
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
public actual class AuthServer actual constructor(host: String, path: String, port: Int) {

    private val deferredRedirectUri = CompletableDeferred<String>()
    private val server = embeddedServer(CIO, port, host) {
        routing {
            get("/$path") {
                deferredRedirectUri.complete(call.request.uri)
                call.respondHtml(AuthSuccess)
                stop()
            }
        }
    }.start()

    @Suppress("HttpUrlsUsage")
    private val redirectUri: String = "http://$host:$port/$path"
    public actual fun getRedirectUri(): String = redirectUri

    public actual suspend fun awaitRedirectUri(duration: Duration): String =
        withTimeout(duration) { deferredRedirectUri.await() }

    private fun stop(gracePeriodMillis: Long = 500L, timeoutMillis: Long = 500L): Unit =
        server.stop(gracePeriodMillis, timeoutMillis)

    private suspend fun ApplicationCall.respondHtml(text: String) =
        respondText(text, ContentType.Text.Html, HttpStatusCode.OK)
}
