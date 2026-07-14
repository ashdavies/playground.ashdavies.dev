@file:OptIn(ExperimentalAtomicApi::class)

package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.AppCheckToken
import dev.ashdavies.http.common.models.FirebaseApp
import dev.ashdavies.http.defaultHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.contentType
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Clock

@Suppress("UnusedReceiverParameter")
private val HttpHeaders.XFirebaseAppCheck: String get() = "X-Firebase-AppCheck"

internal class JvmWasmJsAppCheckAuthProvider(
    private val appId: String,
    tokenClient: HttpClient?,
) : AuthProvider {
    private val tokenClient = tokenClient ?: defaultHttpClient { }
    private val cachedToken = AtomicReference<AppCheckToken?>(null)

    @Deprecated("Please use sendWithoutRequest function instead", ReplaceWith("sendWithoutRequest(request)"))
    override val sendWithoutRequest: Boolean = true

    override fun sendWithoutRequest(request: HttpRequestBuilder): Boolean {
        val pathSegments = request.url.pathSegments
        return pathSegments.none { it == "token" }
    }

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        return auth.authScheme.equals("AppCheck", ignoreCase = true)
    }

    private suspend fun getOrRefreshToken(requestHost: String, forceRefresh: Boolean = false): String {
        val currentEpochMillis = Clock.System.now().toEpochMilliseconds()
        val currentToken = cachedToken.load()
        if (!forceRefresh && currentToken != null && currentToken.ttlMillis > currentEpochMillis + 300_000) {
            return currentToken.token
        }

        val response = tokenClient.post("https://$requestHost/firebase/token") {
            contentType(ContentType.Application.Json)
            setBody(FirebaseApp(appId))
        }.body<AppCheckToken>()

        val expiryTime = currentEpochMillis + response.ttlMillis
        val updatedToken = AppCheckToken(token = response.token, ttlMillis = expiryTime)
        
        cachedToken.store(updatedToken)
        return updatedToken.token
    }

    override suspend fun addRequestHeaders(request: HttpRequestBuilder, authHeader: HttpAuthHeader?) {
        val host = request.url.host
        val token = getOrRefreshToken(host)
        request.headers[HttpHeaders.XFirebaseAppCheck] = token
    }

    override suspend fun refreshToken(response: HttpResponse): Boolean {
        val host = response.call.request.url.host
        getOrRefreshToken(host, forceRefresh = true)
        return true
    }
}

internal actual fun createAppCheckAuthProvider(
    appId: String,
    tokenClient: HttpClient?,
): AuthProvider = JvmWasmJsAppCheckAuthProvider(appId, tokenClient)
