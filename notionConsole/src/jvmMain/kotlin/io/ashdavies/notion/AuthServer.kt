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
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
actual class AuthServer actual constructor(host: String, path: String, port: Int) {

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
    actual fun getRedirectUri(): String = redirectUri

    actual suspend fun awaitRedirectUri(duration: Duration): String =
        withTimeout(duration) { deferredRedirectUri.await() }

    private fun stop(gracePeriodMillis: Long = 500L, timeoutMillis: Long = 500L): Unit =
        server.stop(gracePeriodMillis, timeoutMillis)

    private suspend fun ApplicationCall.respondHtml(text: String) =
        respondText(text, ContentType.Text.Html, HttpStatusCode.OK)
}
