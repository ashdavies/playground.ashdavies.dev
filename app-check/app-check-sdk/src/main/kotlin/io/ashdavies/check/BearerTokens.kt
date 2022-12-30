package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val GOOGLE_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token"

private val FIREBASE_CLAIMS_SCOPES = listOf(
    "https://www.googleapis.com/auth/cloud-platform",
    "https://www.googleapis.com/auth/firebase.database",
    "https://www.googleapis.com/auth/firebase.messaging",
    "https://www.googleapis.com/auth/identitytoolkit",
    "https://www.googleapis.com/auth/userinfo.email",
)

internal suspend fun bearerResponse(
    httpClient: HttpClient,
    algorithm: Algorithm,
    accountId: String,
    appId: String,
): BearerResponse {
    val assertionToken = Jwt.create(algorithm) {
        it.audience = GOOGLE_TOKEN_ENDPOINT
        it.scope = FIREBASE_CLAIMS_SCOPES
        it.issuer = accountId
        it.appId = appId
    }

    return httpClient.post(GOOGLE_TOKEN_ENDPOINT) {
        contentType(ContentType.Application.FormUrlEncoded)
        parameter("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
        parameter("assertion", assertionToken)
    }.body()
}

@Serializable
internal data class BearerResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
)
