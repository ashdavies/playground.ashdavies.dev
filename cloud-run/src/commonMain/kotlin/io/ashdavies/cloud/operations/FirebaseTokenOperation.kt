package io.ashdavies.cloud.operations

import io.ashdavies.check.AppCheck
import io.ashdavies.http.common.models.AppCheckToken
import io.ashdavies.http.common.models.FirebaseApp
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

internal class FirebaseTokenOperation(private val appCheck: AppCheck) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val appCheckRequest = call.receive<FirebaseApp>()
        val appCheckToken = appCheck.createToken(
            appId = appCheckRequest.appId,
            mapper = ::AppCheckToken,
        )

        call.respond(appCheckToken)
    }
}
