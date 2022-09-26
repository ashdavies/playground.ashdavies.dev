package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private val FIREBASE_CLAIMS_SCOPES = listOf(
    "https://www.googleapis.com/auth/cloud-platform",
    "https://www.googleapis.com/auth/firebase.database",
    "https://www.googleapis.com/auth/firebase.messaging",
    "https://www.googleapis.com/auth/identitytoolkit",
    "https://www.googleapis.com/auth/userinfo.email",
)

private const val GOOGLE_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token"

internal fun AuthorisedHttpApplication(content: @Composable HttpScope.() -> Unit) = HttpApplication {
    CompositionLocalProvider(LocalHttpClient provides rememberAuthorisedHttpClient()) {
        content()
    }
}

@Composable
private fun rememberAuthorisedHttpClient(): HttpClient {
    val config: HttpClientConfig = rememberHttpClientConfig()
    val client: HttpClient = LocalHttpClient.current

    return remember(config, client) {
        AuthorisedHttpClient(client) {
            client.bearerTokens(config)
        }
    }
}

private fun AuthorisedHttpClient(from: HttpClient, loadTokens: suspend () -> BearerTokens?): HttpClient = from.config {
    install(Auth) {
        bearer {
            loadTokens { loadTokens() }
        }
    }
}

private suspend fun HttpClient.bearerTokens(config: HttpClientConfig): BearerTokens {
    val jwt = Jwt.create(config.algorithm) {
        it.audience = GOOGLE_TOKEN_ENDPOINT
        it.scope = FIREBASE_CLAIMS_SCOPES
        it.issuer = config.accountId
        it.appId = config.appId
    }

    val response: HttpResponse = post(GOOGLE_TOKEN_ENDPOINT) {
        contentType(ContentType.Application.FormUrlEncoded)
        grantType(JwtBearer)
        assertion(jwt)
    }

    val bearer: BearerResponse = response.body()
    val accessToken: String = bearer
        .accessToken
        .substring(0..240)

    return BearerTokens(
        accessToken = accessToken,
        refreshToken = "",
    )
}

internal data class HttpClientConfig(
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

@Composable
private fun rememberHttpClientConfig(
    query: AppCheckQuery = rememberAppCheckQuery(),
    signer: CryptoSigner = rememberCryptoSigner(),
    algorithm: Algorithm = rememberAlgorithm()
) = HttpClientConfig(
    accountId = signer.getAccountId(),
    algorithm = algorithm,
    appId = query.appId,
)
