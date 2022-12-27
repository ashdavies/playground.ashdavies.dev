package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val GOOGLE_AUTH_SCOPE = "https://www.googleapis.com/auth"
private const val GOOGLE_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token"

private val FIREBASE_CLAIMS_SCOPES = listOf(
    "$GOOGLE_AUTH_SCOPE/cloud-platform",
    "$GOOGLE_AUTH_SCOPE/firebase.database",
    "$GOOGLE_AUTH_SCOPE/firebase.messaging",
    "$GOOGLE_AUTH_SCOPE/identitytoolkit",
    "$GOOGLE_AUTH_SCOPE/userinfo.email",
)

public fun AuthorisedHttpClient(from: HttpClient, config: HttpClientConfig): HttpClient =
    from.config {
        install(Auth) {
            bearer {
                loadTokens { bearerTokens(from, config) }
            }
        }
    }

private suspend fun bearerTokens(client: HttpClient, config: HttpClientConfig): BearerTokens {
    val response = client.post(GOOGLE_TOKEN_ENDPOINT) {
        contentType(ContentType.Application.FormUrlEncoded)
        assertion(createJwt(config))
        grantType(JwtBearer)
    }.body<BearerResponse>()

    return BearerTokens(
        accessToken = response.accessToken,
        refreshToken = "null",
    )
}

private fun createJwt(config: HttpClientConfig): String = Jwt.create(config.algorithm) {
    it.audience = GOOGLE_TOKEN_ENDPOINT
    it.scope = FIREBASE_CLAIMS_SCOPES
    it.issuer = config.accountId
    it.appId = config.appId
}

public data class HttpClientConfig(
    val algorithm: Algorithm,
    val accountId: String,
    val appId: String,
)

@Serializable
internal data class BearerResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
)
