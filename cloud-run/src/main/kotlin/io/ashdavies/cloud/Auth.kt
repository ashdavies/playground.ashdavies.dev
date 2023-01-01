package io.ashdavies.cloud

import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable

private const val SIGNUP_ENDPOINT =
    "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken"

internal fun Routing.auth(client: HttpClient) {
    post("/auth") {
        val firebaseAuth = FirebaseAuth.getInstance(firebaseApp)
        val accountRequest = call.receive<SignInRequest>()
        val customToken = firebaseAuth.createCustomToken(
            /* uid = */ accountRequest.uid,
        )

        val authResult = client.post(SIGNUP_ENDPOINT) {
            setBody(mapOf("token" to customToken, "returnSecureToken" to "true"))
            parameter("key", call.request.header("X-API-Key"))
            contentType(ContentType.Application.Json)
        }.body<SignInResponse>()

        call.respond(authResult)
    }
}

@Serializable
internal data class SignInRequest(
    val uid: String
)

@Serializable
internal data class SignInResponse(
    val refreshToken: String,
    val expiresIn: Long,
    val idToken: String,
)
