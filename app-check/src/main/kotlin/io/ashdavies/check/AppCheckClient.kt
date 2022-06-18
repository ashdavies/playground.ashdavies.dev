package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import com.google.auth.oauth2.ServiceAccountCredentials
import io.ashdavies.check.AppCheckConstants.APP_CHECK_V1_API
import io.ashdavies.check.AppCheckConstants.FIREBASE_CLAIMS_SCOPES
import io.ashdavies.check.AppCheckConstants.GOOGLE_TOKEN_ENDPOINT
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.compose.Remember
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private val HttpStatusCode.isError: Boolean
    get() = value in (400 until 600)

internal class AppCheckClient @Remember constructor(private val client: HttpClient, private val config: Config) {

    suspend fun exchangeToken(token: String, request: AppCheckRequest): AppCheckGenerator.Token {
        val urlString = "$APP_CHECK_V1_API/${config.projectId}/apps/${request.appId}:exchangeCustomToken"
        val response: HttpResponse = client.post(urlString) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("customToken" to token))
            bearerAuth(getBearerToken(request))
        }

        if (response.status.isError) {
            throw HttpException(response)
        }

        val result: AppCheckResponse = response.body()
        val ttlMillis: Int = result.ttl
            .substring(0, result.ttl.length - 1)
            .toInt() * 1000

        return AppCheckGenerator.Token(result.token, ttlMillis)
    }

    private suspend fun getBearerToken(request: AppCheckRequest): String {
        val jwt = Jwt.create(config.algorithm) {
            audience = GOOGLE_TOKEN_ENDPOINT
            scope = FIREBASE_CLAIMS_SCOPES
            issuer = config.clientEmail
            appId = request.appId
        }

        val response: HttpResponse = client.post(GOOGLE_TOKEN_ENDPOINT) {
            contentType(ContentType.Application.FormUrlEncoded)
            grantType(JwtBearer)
            assertion(jwt)
        }

        return response
            .body<BearerResponse>()
            .accessToken
            .substring(0..240)
    }

    data class Config @Remember constructor(
        val algorithm: Algorithm,
        val clientEmail: String,
        val projectId: String,
    )

    @Serializable
    data class AppCheckResponse(
        @SerialName("token") val token: String,
        @SerialName("ttl") val ttl: String,
    )

    @Serializable
    data class BearerResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("token_type") val tokenType: String,
        @SerialName("expires_in") val expiresIn: Int,
    )
}

private suspend fun HttpException(response: HttpResponse): HttpException {
    return HttpException(response.status.value, response.body())
}

@Composable
internal fun rememberAppCheckClient(
    config: AppCheckClient.Config = rememberAppCheckClientConfig(),
    client: HttpClient = LocalHttpClient.current
): AppCheckClient = remember(config) {
    AppCheckClient(
        config = config,
        client = client,
    )
}

@Composable
private fun rememberAppCheckClientConfig(
    credentials: ServiceAccountCredentials = rememberGoogleCredentials(),
    algorithm: Algorithm = rememberAlgorithm(credentials),
): AppCheckClient.Config = remember(credentials) {
    AppCheckClient.Config(
        clientEmail = credentials.clientEmail,
        projectId = credentials.projectId,
        algorithm = algorithm,
    )
}
