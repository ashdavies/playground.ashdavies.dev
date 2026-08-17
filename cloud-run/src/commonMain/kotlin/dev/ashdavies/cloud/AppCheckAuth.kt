package dev.ashdavies.cloud

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

internal fun AuthenticationConfig.appCheck() {
    jwt(CONFIGURATION_NAME) {
        authHeader { call ->
            val token = call.request.headers[HttpHeaders.XFirebaseAppCheck]
            if (token?.isNotEmpty() == true) {
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
                    "error_description=\"Missing or invalid X-Firebase-AppCheck header\"",
            )

            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = mapOf(
                    "code" to "APP_CHECK_FAILED",
                    "message" to "401 Unauthorized: Valid 'X-Firebase-AppCheck' header required.",
                ),
            )
        }

        realm = "Firebase"

        validate { credential ->
            if (credential.payload.subject.isNotEmpty()) {
                JWTPrincipal(credential.payload)
            } else {
                application.log.warn("[AppCheck] Auth failed: JWT payload subject is empty")
                null
            }
        }

        verifier(UrlJwkProvider(URI("https://firebaseappcheck.googleapis.com/v1/jwks").toURL())) {
            val projectNumber = requireNotNull(BuildConfig.PROJECT_NUMBER) { "PROJECT_NUMBER was null" }
            withIssuer("https://firebaseappcheck.googleapis.com/$projectNumber")
        }
    }
}
