package io.ashdavies.playground.check

import io.ashdavies.playground.check.AppCheckToken.Type
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

private const val FIREBASE_APP_CHECK_V1_API_ENDPOINT = "https://firebaseappcheck.googleapis.com/v1beta/projects"

internal class AppCheckClient(private val httpClient: HttpClient, private val projectId: String) {

    suspend fun exchangeToken(customToken: String, appId: String, type: Type = Type.Custom): AppCheckToken {
        val urlString = "$FIREBASE_APP_CHECK_V1_API_ENDPOINT/$projectId/apps/$appId:exchange${type.name}Token"

        val response: AppCheckClientResponse = httpClient.post(urlString) {
            headers { append("X-Firebase-Client", "fire-admin-node/10.1.0") }
            setBody(mapOf("customToken" to customToken))
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

@Serializable
private data class AppCheckClientResponse(val token: String, val ttl: String)
