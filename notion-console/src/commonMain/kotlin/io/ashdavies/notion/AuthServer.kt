package io.ashdavies.notion

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.oauth
import io.ktor.server.auth.principal
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.toMap
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

public data class StubReceiver(val value: String)

internal suspend fun getAccessToken(provider: OAuthProvider): AccessToken {
    return suspendCancellableCoroutine { continuation ->
        val server: CIOApplicationEngine = embeddedServer(CIO, port = 8080) {
            install(Authentication) { configure(provider) }
            install(CallLogging)

            routing {
                oauth(continuation::resume)
            }
        }

        server.start(wait = true)
    }
}

private fun AuthenticationConfig.configure(provider: OAuthProvider) {
    oauth("auth-oauth-google") {
        urlProvider = { "http://localhost:8080/callback" }
        providerLookup = { OAuthSettings(provider) }
        client = HttpClient {
            install(ContentNegotiation, ContentNegotiation.Config::json)
            install(Logging) { level = LogLevel.ALL }
        }
    }
}

private fun Routing.oauth(callback: (AccessToken) -> Unit) {
    authenticate("auth-oauth-google") {
        get("/login") {/* Redirects to "authorizeUrl" automatically */ }
        get("/callback") { callback(callback) }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.callback(callback: (AccessToken) -> Unit) {
    val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
    if (principal != null) {
        call.response.headers.append(HttpHeaders.Connection, "close")
        call.respond(HttpStatusCode.NoContent)
        callback(AccessToken(principal))
    }
}

private fun AccessToken(principal: OAuthAccessTokenResponse.OAuth2) = AccessToken(
    extraParameters = principal.extraParameters.toMap(),
    refreshToken = principal.refreshToken,
    accessToken = principal.accessToken,
    tokenType = principal.tokenType,
    expiresIn = principal.expiresIn,
)
