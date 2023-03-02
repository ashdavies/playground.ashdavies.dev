package io.ashdavies.cloud

import com.google.firebase.auth.FirebaseAuth
import io.ashdavies.playground.models.AuthResult
import io.ashdavies.playground.models.SignInRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

private const val SIGNUP_ENDPOINT =
    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken"

internal fun Route.auth(client: HttpClient) {
    post("/auth") {
        val firebaseAuth = FirebaseAuth.getInstance(firebaseApp)
        val accountRequest = call.receive<SignInRequest>()
        val customToken = firebaseAuth.createCustomToken(
            accountRequest.uid,
        )

        val authResponse = client.post(SIGNUP_ENDPOINT) {
            setBody(mapOf("token" to customToken, "returnSecureToken" to "true"))
            parameter("key", call.request.header("X-API-Key"))
        }.body<AuthResult>()

        call.respond(authResponse)
    }
}
