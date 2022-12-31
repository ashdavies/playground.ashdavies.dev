package io.ashdavies.cloud

import io.ashdavies.check.AppCheckRequest
import io.ashdavies.check.appCheck
import io.ashdavies.http.DefaultHttpClient
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

internal fun Routing.token(httpClient: HttpClient = DefaultHttpClient()) {
    val appCheck = firebaseApp.appCheck(httpClient)

    post("/token") {
        val appCheckRequest = call.receive<AppCheckRequest>()
        val appCheckToken = appCheck.createToken(
            appId = appCheckRequest.appId,
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = appCheckToken,
        )
    }

    post("/token:verify") {
        val appCheckToken = requireNotNull(call.request.header("X-Firebase-AppCheck")) {
            "Request is missing app check token header"
        }

        call.respond(
            message = appCheck.verifyToken(appCheckToken),
            status = HttpStatusCode.OK,
        )
    }
}
