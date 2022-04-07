package io.ashdavies.playground

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.oauth
import io.ktor.server.auth.principal
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.CallLogging
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import io.ktor.client.engine.cio.CIO as ClientCIO
import io.ktor.server.cio.CIO as ServerCIO

private const val GRACE_PERIOD_MILLIS = 1000L
private const val TIMEOUT_MILLIS = 5000L

public actual val accessToken: Flow<AccessToken>
    get() = authenticationServer()

@OptIn(ExperimentalCoroutinesApi::class)
private fun authenticationServer(): Flow<AccessToken> = channelFlow {
    val server: CIOApplicationEngine = embeddedServer(ServerCIO, port = 8080) {
        install(Authentication) {
            oauth("auth-oauth-google") {
                providerLookup = { OAuthSettings(OAuthProvider.Google) }
                urlProvider = { "http://localhost:8080/callback" }
                client = HttpClient(ClientCIO) {
                    install(ContentNegotiation) {
                        json()
                    }

                    install(Logging)
                }
            }
        }

        install(CallLogging)

        routing {
            authenticate("auth-oauth-google") {
                get("/callback") {
                    val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                    if (principal != null) send(AccessToken(principal))
                    call.respondText("Success!")
                    cancel()
                }
            }
        }
    }

    invokeOnClose {
        server.stop(GRACE_PERIOD_MILLIS, TIMEOUT_MILLIS)
    }

    server.start(wait = false)
}

private fun AccessToken(principal: OAuthAccessTokenResponse.OAuth2) = AccessToken(
    refreshToken = principal.refreshToken,
    accessToken = principal.accessToken,
    tokenType = principal.tokenType,
    expiresIn = principal.expiresIn,
)
