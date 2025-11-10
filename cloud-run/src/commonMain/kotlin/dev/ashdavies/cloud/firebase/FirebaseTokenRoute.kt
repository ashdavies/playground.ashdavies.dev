package dev.ashdavies.cloud.firebase

import dev.ashdavies.check.AppCheck
import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.cloud.google.GoogleApiException
import dev.ashdavies.http.common.models.AppCheckToken
import dev.ashdavies.http.common.models.FirebaseApp
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

@ContributesIntoSet(AppScope::class, binding<CloudRunRoute>())
internal class FirebaseTokenRoute @Inject constructor(
    private val appCheck: AppCheck,
) : CloudRunRoute {

    override fun Routing.invoke(): Route = post("/firebase/token") {
        try {
            val appCheckRequest = call.receive<FirebaseApp>()
            val appCheckToken = appCheck.createToken(
                appId = appCheckRequest.appId,
                mapper = ::AppCheckToken,
            )

            call.respond(appCheckToken)
        } catch (exception: GoogleApiException) {
            call.respond(
                status = HttpStatusCode.fromValue(exception.error.code),
                message = exception.error,
            )
        }
    }
}
