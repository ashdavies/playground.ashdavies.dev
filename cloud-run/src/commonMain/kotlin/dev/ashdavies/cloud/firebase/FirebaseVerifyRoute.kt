package dev.ashdavies.cloud.firebase

import dev.ashdavies.check.AppCheck
import dev.ashdavies.check.AppCheckToken
import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.http.common.models.DecodedToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.put

@ContributesIntoSet(AppScope::class, binding<CloudRunRoute>())
internal class FirebaseVerifyRoute @Inject constructor(
    private val appCheck: AppCheck,
) : CloudRunRoute {

    override fun Routing.invoke() = put("/firebase/token:verify") {
        when (val appCheckToken = call.request.header(HttpHeaders.AppCheckToken)) {
            null -> call.respond(HttpStatusCode.BadRequest, "Request is missing app check token header")
            else -> {
                val decodedToken = appCheck.verifyToken(
                    token = appCheckToken,
                    mapper = ::DecodedToken,
                )

                call.respond(decodedToken)
            }
        }
    }
}
