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
        val appCheckToken = appCheck.createToken(
            appId = appCheckRequest.appId,
            mapper = ::AppCheckToken,
        )

        call.respond(appCheckToken)
    }

    put("/token:verify") {
        val appCheck = firebaseApp.appCheck(client)
        val appCheckToken = requireNotNull(call.request.header("X-Firebase-AppCheck")) {
            "Request is missing app check token header"
        }

        val decodedToken = appCheck.verifyToken(
            token = appCheckToken,
            mapper = ::DecodedToken,
        )

        call.respond(decodedToken)
    }
}
