package io.ashdavies.cloud.operations

import com.google.firebase.auth.FirebaseAuth
import io.ashdavies.http.common.models.AuthResult
import io.ashdavies.http.common.models.SignInRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond

private const val IDENTITY_TOOLKIT_BASE_URL = "identitytoolkit.googleapis.com/v1/"
private const val SIGN_IN_WITH_CUSTOM_TOKEN = "accounts:signInWithCustomToken"

internal class FirebaseAuthOperation(
    private val firebaseAuth: FirebaseAuth,
    private val httpClient: HttpClient,
) : UnaryOperation {

    override suspend fun invoke(call: ApplicationCall) {
        val accountRequest = call.receive<SignInRequest>()
        val customToken = firebaseAuth.createCustomToken(
            accountRequest.uid,
        )

        val urlString = "https://$IDENTITY_TOOLKIT_BASE_URL$SIGN_IN_WITH_CUSTOM_TOKEN"
        val authResponse = httpClient.post(urlString) {
            setBody(mapOf("token" to customToken, "returnSecureToken" to "true"))
            parameter("key", call.request.header("X-API-Key"))
        }.body<AuthResult>()

        call.respond(authResponse)
    }
}
