package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ashdavies.check.AppCheckConstants.APP_CHECK_V1_API
import io.ashdavies.check.AppCheckConstants.FIREBASE_CLAIMS_SCOPES
import io.ashdavies.check.AppCheckConstants.GOOGLE_TOKEN_ENDPOINT
import io.ashdavies.playground.cloud.HttpException
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

internal class AppCheckClient(private val httpClient: HttpClient, private val config: Config) {
    suspend fun exchangeToken(token: String, request: io.ashdavies.check.AppCheckRequest): AppCheckToken {
        val urlString = "$APP_CHECK_V1_API/${config.projectId}/apps/${request.appId}:exchangeCustomToken"
        val response: HttpResponse = httpClient.post(urlString) {
            contentType(ContentType.Application.Json)
            bearerAuth(getBearerToken(request))
            setBody(AppCheckRequest(token))
        }

        if (response.status.isError) {
            throw HttpException(response)
        }

        val result: AppCheckResponse = response.body()
        val ttlMillis: Int = result.ttl
            .substring(0, result.ttl.length - 1)
            .toInt() * 1000

        return AppCheckToken(result.token, ttlMillis)
    }

    private suspend fun getBearerToken(request: io.ashdavies.check.AppCheckRequest): String {
        val jwt = Jwt.create(config.algorithm) {
            audience = GOOGLE_TOKEN_ENDPOINT
            scope = FIREBASE_CLAIMS_SCOPES
            issuer = config.clientEmail
            appId = request.appId
        }

        val response: HttpResponse = httpClient.post(GOOGLE_TOKEN_ENDPOINT) {
            setBody("grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=$jwt")
            contentType(ContentType.Application.FormUrlEncoded)
        }

        return response
            .body<BearerResponse>()
            .accessToken
            .substring(0..240)
    }

    data class Config(
        val algorithm: Algorithm,
        val clientEmail: String,
        val projectId: String,
    )

    @Serializable
    data class AppCheckRequest(
        @SerialName("customToken") val customToken: String
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

private suspend fun HttpException(response: HttpResponse) = HttpException(response.status.value, response.body())
