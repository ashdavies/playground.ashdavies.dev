package dev.ashdavies.cloud

import com.auth0.jwk.UrlJwkProvider
import dev.ashdavies.check.XFirebaseAppCheck
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import java.net.URI

private const val FIREBASE_APP_CHECK_API = "firebaseappcheck.googleapis.com"
private const val CONFIGURATION_NAME = "app-check"

internal fun Route.appCheckAuthentication(build: Route.() -> Unit): Route {
    return authenticate(CONFIGURATION_NAME, build = build)
}

internal fun AuthenticationConfig.appCheck(projectNumber: String) {
    jwt(CONFIGURATION_NAME) {
        authHeader { call ->
            val token = call.request.headers[HttpHeaders.XFirebaseAppCheck]
            if (token != null) {
                HttpAuthHeader.Single("Bearer", token)
            } else {
                null
            }
        }

        challenge { _, _ ->
            call.response.headers.append(HttpHeaders.WWWAuthenticate, "AppCheck realm=\"Firebase App Check\"")
            call.respond(HttpStatusCode.Unauthorized, "Missing or invalid App Check token")
        }

        validate { credential ->
            if (credential.payload.subject.isNotEmpty()) {
                JWTPrincipal(credential.payload)
            } else {
                null
            }
        }

        verifier(UrlJwkProvider(URI("https://$FIREBASE_APP_CHECK_API/v1/jwks").toURL())) {
            withIssuer("https://$FIREBASE_APP_CHECK_API/$projectNumber")
        }
    }
}
