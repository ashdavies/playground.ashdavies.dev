package io.ashdavies.cloud

import io.ashdavies.check.appCheck
import io.ashdavies.playground.models.AppCheckToken
import io.ashdavies.playground.models.DecodedToken
import io.ashdavies.playground.models.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put

internal fun Route.token(client: HttpClient) {
    post("/token") {
        val appCheck = firebaseApp.appCheck(client)
        val appCheckRequest = call.receive<FirebaseApp>()
        val appCheckResponse = appCheck.createToken(
            appId = appCheckRequest.appId,
        )

        val appCheckToken = AppCheckToken(
            ttlMillis = appCheckResponse.ttlMillis,
            token = appCheckResponse.token,
        )

        call.respond(appCheckToken)
    }

    put("/token:verify") {
        val appCheck = firebaseApp.appCheck(client)
        val appCheckToken = requireNotNull(call.request.header("X-Firebase-AppCheck")) {
            "Request is missing app check token header"
        }

        val appCheckResponse = appCheck.verifyToken(appCheckToken)
        val decodedToken = DecodedToken(
            audience = appCheckResponse.audience,
            expiresAt = appCheckResponse.expiresAt,
            subject = appCheckResponse.subject,
            issuedAt = appCheckResponse.issuedAt,
            issuer = appCheckResponse.issuer,
            appId = appCheckResponse.appId,
        )

        call.respond(decodedToken)
    }
}
