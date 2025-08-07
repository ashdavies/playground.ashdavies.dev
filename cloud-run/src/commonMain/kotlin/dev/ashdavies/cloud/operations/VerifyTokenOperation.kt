package dev.ashdavies.cloud.operations

import dev.ashdavies.check.AppCheck
import dev.ashdavies.check.AppCheckToken
import dev.ashdavies.http.common.models.DecodedToken
import io.ktor.http.HttpHeaders
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header
import io.ktor.server.response.respond

internal class VerifyTokenOperation(private val appCheck: AppCheck) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val appCheckToken = requireNotNull(call.request.header(HttpHeaders.AppCheckToken)) {
            "Request is missing app check token header"
        }

        val decodedToken = appCheck.verifyToken(
            token = appCheckToken,
            mapper = ::DecodedToken,
        )

        call.respond(decodedToken)
    }
}
