package dev.ashdavies.cloud

import com.auth0.jwk.UrlJwkProvider
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.Route
import java.net.URI

private const val CONFIGURATION_NAME = "app-check"

internal fun Route.appCheckAuthentication(build: Route.() -> Unit): Route {
    return authenticate(CONFIGURATION_NAME, build = build)
}

internal fun AuthenticationConfig.appCheck() {
    jwt(CONFIGURATION_NAME) {
        val projectNumber = requireNotNull(BuildConfig.APP_ID?.split(":")?.getOrNull(1)) {
            "APP_ID is missing or invalid in BuildConfig"
        }
        verifier(UrlJwkProvider(URI("https://firebaseappcheck.googleapis.com/v1/jwks").toURL())) {
            withIssuer("https://firebaseappcheck.googleapis.com/$projectNumber")
        }
        authHeader { call ->
            val token = call.request.headers["X-Firebase-AppCheck"]
            if (token != null) {
                HttpAuthHeader.Single("Bearer", token)
            } else {
                null
            }
        }
        validate { credential ->
            if (credential.payload.subject.isNotEmpty()) {
                JWTPrincipal(credential.payload)
            } else {
                null
            }
        }
    }
}