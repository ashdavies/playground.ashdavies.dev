package dev.ashdavies.cloud.firebase

import com.google.firebase.auth.FirebaseAuth
import dev.ashdavies.cloud.CloudRunRoute
import dev.ashdavies.http.common.models.AuthResult
import dev.ashdavies.http.common.models.SignInRequest
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

private const val IDENTITY_TOOLKIT_BASE_URL = "identitytoolkit.googleapis.com/v1/"
private const val SIGN_IN_WITH_CUSTOM_TOKEN = "accounts:signInWithCustomToken"

@ContributesIntoSet(AppScope::class, binding<CloudRunRoute>())
internal class FirebaseAuthRoute @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val httpClient: HttpClient,
) : CloudRunRoute {

    override fun Routing.invoke() = post("/firebase/auth") {
        val accountRequest = call.receive<SignInRequest>()
        val customToken = firebaseAuth.createCustomToken(
            accountRequest.uid,
        )

        val urlString = "https://${IDENTITY_TOOLKIT_BASE_URL}${SIGN_IN_WITH_CUSTOM_TOKEN}"
        val authResponse = httpClient.post(urlString) {
            setBody(mapOf("token" to customToken, "returnSecureToken" to "true"))
            parameter("key", call.request.header("X-API-Key"))
        }.body<AuthResult>()

        call.respond(authResponse)
    }
}
