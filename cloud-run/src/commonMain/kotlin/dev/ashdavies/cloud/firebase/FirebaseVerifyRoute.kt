package dev.ashdavies.cloud.firebase

import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.cloud.appCheckAuthentication
import dev.ashdavies.http.common.models.DecodedToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.put

@ContributesIntoSet(AppScope::class, binding<CloudRunRoute>())
internal class FirebaseVerifyRoute @Inject constructor() : CloudRunRoute {

    override fun Routing.invoke() = appCheckAuthentication {
        put("/firebase/token:verify") {
            val principal = requireNotNull(call.principal<JWTPrincipal>())
            val payload = principal.payload
            val decodedToken = DecodedToken(
                audience = payload.audience,
                expiresAt = payload.expiresAtAsInstant.epochSecond,
                issuedAt = payload.issuedAtAsInstant.epochSecond,
                subject = payload.subject,
                issuer = payload.issuer,
                appId = payload.subject,
            )

            call.respond(decodedToken)
        }
    }
}
