package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ashdavies.check.AppCheckConstants.FIREBASE_APP_CHECK_V1_API_ENDPOINT
import io.ashdavies.check.AppCheckConstants.FIREBASE_CLAIMS_SCOPES
import io.ashdavies.check.AppCheckConstants.GOOGLE_TOKEN_AUDIENCE
import io.ashdavies.playground.cloud.HttpException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

private val HttpStatusCode.isError: Boolean
    get() = value in (400 until 600)

internal class AppCheckClient(private val httpClient: HttpClient, private val config: Config) {
    suspend fun exchangeToken(token: String, request: AppCheckRequest): AppCheckToken {
        val urlString = "$FIREBASE_APP_CHECK_V1_API_ENDPOINT/${config.projectId}/apps/${request.appId}:exchangeCustomToken"
        val response: HttpResponse = httpClient.post(urlString) {
            contentType(ContentType.Application.Json)
            bearerAuth(getBearerToken(request))
            setBody(AppCheckClientBody(token))
        }

        if (response.status.isError) {
            throw HttpException(response)
        }

        val result: AppCheckClientResponse = response.body()
        val ttlMillis: Int = result.ttl
            .substring(0, result.ttl.length - 1)
            .toInt() * 1000

        return AppCheckToken(result.token, ttlMillis)
    }

    private suspend fun getBearerToken(request: AppCheckRequest): String {
        val jwt = Jwt.create(config.algorithm) {
            audience = GOOGLE_TOKEN_AUDIENCE
            scope = FIREBASE_CLAIMS_SCOPES
            issuer = config.clientEmail
            appId = request.appId
        }

        val response: HttpResponse = httpClient.post(GOOGLE_TOKEN_AUDIENCE) {
            setBody("grant_type=urn%3Aietf%3Aparams%3Aoauth%3Agrant-type%3Ajwt-bearer&assertion=$jwt")
            contentType(ContentType.Application.FormUrlEncoded)
        }

        return response
            .body<Response>()
            .accessToken
            .substring(0..240)
    }

    data class Config(
        val algorithm: Algorithm,
        val clientEmail: String,
        val projectId: String,
    )

    @Serializable
    @OptIn(ExperimentalSerializationApi::class)
    data class Response(
        @JsonNames("access_token") val accessToken: String,
        @JsonNames("token_type") val tokenType: String,
        @JsonNames("expires_in") val expiresIn: Int,
    )
}

private fun getUrlString(projectId: String, request: AppCheckRequest, method: String): String =
    "$FIREBASE_APP_CHECK_V1_API_ENDPOINT/$projectId/apps/${request.appId}:$method"

private suspend fun HttpException(response: HttpResponse) = HttpException(response.status.value, response.body())

private suspend inline fun <reified T : Any> HttpClient.post(
    urlString: String, body: T, block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = post {
    contentType(ContentType.Application.Json)
    url(urlString)
    setBody(body)
    block()
}

@Serializable
private data class AppCheckClientBody(val customToken: String)

@Serializable
private data class AppCheckClientResponse(val token: String, val ttl: String)
