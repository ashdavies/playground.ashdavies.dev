package dev.ashdavies.cloud.appcheck

import com.auth0.jwk.UrlJwkProvider
import dev.ashdavies.http.common.models.AppCheck
import dev.ashdavies.http.common.models.XFirebaseAppCheck
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.application.log
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import java.net.URI

private const val CONFIGURATION_NAME = "app-check"

internal fun Route.appCheckAuthentication(build: Route.() -> Unit): Route {
    return authenticate(CONFIGURATION_NAME, build = build)
}

internal fun AuthenticationConfig.appCheck(projectNumber: String) {
    jwt(CONFIGURATION_NAME) {
        authHeader { call ->
            val token = call.request.headers[HttpHeaders.XFirebaseAppCheck]

            if (!token.isNullOrEmpty()) {
                HttpAuthHeader.Single(AuthScheme.AppCheck, token)
            } else {
                call.application.log.warn("[AppCheck] Auth failed: '${HttpHeaders.XFirebaseAppCheck}' header is missing or blank")
                null
            }
        }

        challenge { defaultScheme, realm ->
            call.response.header(
                name = HttpHeaders.WWWAuthenticate,
                value = "$defaultScheme realm=\"$realm\", " +
                    "error=\"unauthorized\", " +
                    "error_description=\"Missing or invalid '${HttpHeaders.XFirebaseAppCheck}' header\"",
            )

            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = mapOf(
                    "code" to "APP_CHECK_FAILED",
                    "message" to "401 Unauthorized: Valid '${HttpHeaders.XFirebaseAppCheck}' header required.",
                ),
            )
        }

        authSchemes(AuthScheme.AppCheck)
        realm = "Firebase"

        validate { credential ->
            if (!credential.payload.subject.isNullOrEmpty()) {
                JWTPrincipal(credential.payload)
            } else {
                application.log.warn("[AppCheck] Validate failed: subject (App ID) was null or empty")
                null
            }
        }

        verifier(UrlJwkProvider(URI("https://firebaseappcheck.googleapis.com/v1/jwks").toURL())) {
            withIssuer("https://firebaseappcheck.googleapis.com/$projectNumber")
        }
    }
}
