package io.ashdavies.check

import io.ashdavies.check.AppCheckToken.Type
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

private const val FIREBASE_APP_CHECK_V1_API_ENDPOINT = "https://firebaseappcheck.googleapis.com/v1beta/projects"

internal class AppCheckClient(private val httpClient: HttpClient, private val projectId: String) {
    suspend fun exchangeToken(token: String, appId: String, type: Type = Type.Custom): AppCheckToken {
        val urlString = "$FIREBASE_APP_CHECK_V1_API_ENDPOINT/$projectId/apps/$appId:exchange${type.name}Token"

        val response: AppCheckClientResponse = httpClient.post(urlString) {
            headers { append("X-Firebase-Client", "fire-admin-node/10.1.0") }
            contentType(ContentType.Application.Json)
            setBody(AppCheckClientBody(token, type))
        }.body()

        val ttlMillis = response.ttl
            .substring(0, response.ttl.length - 1)
            .toInt() * 1000

        return AppCheckToken(
            token = response.token,
            ttlMillis = ttlMillis,
        )
    }
}

private sealed interface AppCheckClientBody {
    @Serializable data class Custom(val customToken: String) : AppCheckClientBody
    @Serializable data class Debug(val debugToken: String) : AppCheckClientBody
}

private fun AppCheckClientBody(token: String, type: Type = Type.Custom): AppCheckClientBody = when (type) {
    Type.Custom -> AppCheckClientBody.Custom(token)
    Type.Debug -> AppCheckClientBody.Debug(token)
}

@Serializable
private data class AppCheckClientResponse(val token: String, val ttl: String)
