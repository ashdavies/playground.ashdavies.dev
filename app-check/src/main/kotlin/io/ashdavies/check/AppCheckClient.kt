package io.ashdavies.check

import io.ashdavies.playground.cloud.HttpException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

private const val FIREBASE_APP_CHECK_V1_API_ENDPOINT = "https://firebaseappcheck.googleapis.com/v1beta/projects"

private fun HttpStatusCode.isError(): Boolean = value in (400 until 600)

internal class AppCheckClient(private val httpClient: HttpClient, private val projectNumber: String) {
    suspend fun exchangeToken(token: String, appId: String): AppCheckToken {
        val urlString = getUrlString(projectNumber, appId, "exchangeCustomToken", System.getenv("PLAYGROUND_API_KEY"))
        val response: HttpResponse = httpClient.post(urlString, AppCheckClientBody(token))

        if (response.status.isError()) {
            throw HttpException(response)
        }

        val result: AppCheckClientResponse = response.body()
        val ttlMillis: Int = result.ttl
            .substring(0, result.ttl.length - 1)
            .toInt() * 1000

        return AppCheckToken(
            token = result.token,
            ttlMillis = ttlMillis,
        )
    }
}

private fun getUrlString(projectId: String, appId: String, method: String, key: String): String =
    "$FIREBASE_APP_CHECK_V1_API_ENDPOINT/$projectId/apps/$appId:$method?key=$key"

private suspend fun HttpException(response: HttpResponse) = HttpException(response.status.value, response.body())

private suspend inline fun <reified T : Any> HttpClient.post(
    urlString: String, body: T, block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = post {
    headers { append("X-Firebase-Client", "fire-admin-node/10.1.0") }
    contentType(ContentType.Application.Json)
    url(urlString)
    setBody(body)
    block()
}

@Serializable
private data class AppCheckClientBody(val customToken: String)

@Serializable
private data class AppCheckClientResponse(val token: String, val ttl: String)
