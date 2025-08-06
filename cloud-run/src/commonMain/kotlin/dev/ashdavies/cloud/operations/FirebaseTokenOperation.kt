package dev.ashdavies.cloud.operations

import dev.ashdavies.cloud.google.GoogleApiException
import dev.ashdavies.http.common.models.AppCheckToken
import dev.ashdavies.http.common.models.FirebaseApp
import io.ashdavies.check.AppCheck
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

internal class FirebaseTokenOperation(private val appCheck: AppCheck) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) = try {
        val appCheckRequest = call.receive<FirebaseApp>()
        val appCheckToken = appCheck.createToken(
            appId = appCheckRequest.appId,
            mapper = ::AppCheckToken,
        )

        call.respond(appCheckToken)
    } catch (exception: GoogleApiException) {
        val status = HttpStatusCode.fromValue(exception.error.code)
        call.respond(status, exception.error)
    }
}
