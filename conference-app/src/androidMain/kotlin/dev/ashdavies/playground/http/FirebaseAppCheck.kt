package dev.ashdavies.playground.http

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader
import kotlinx.coroutines.tasks.await

@Suppress("UnusedReceiverParameter")
private val HttpHeaders.XFirebaseAppCheck: String get() = "X-Firebase-AppCheck"

internal class AndroidAppCheckAuthProvider : AuthProvider {
    @Deprecated("Please use sendWithoutRequest function instead", ReplaceWith("sendWithoutRequest(request)"))
    override val sendWithoutRequest: Boolean = true

    override fun sendWithoutRequest(request: HttpRequestBuilder): Boolean = true

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        return auth.authScheme.equals("AppCheck", ignoreCase = true)
    }

    override suspend fun addRequestHeaders(request: HttpRequestBuilder, authHeader: HttpAuthHeader?) {
        val token = com.google.firebase.appcheck.FirebaseAppCheck.getInstance()
            .getAppCheckToken(false)
            .await()
            .token
        request.headers[HttpHeaders.XFirebaseAppCheck] = token
    }

    override suspend fun refreshToken(response: HttpResponse): Boolean {
        com.google.firebase.appcheck.FirebaseAppCheck.getInstance()
            .getAppCheckToken(true)
            .await()
        return true
    }
}

internal actual fun createAppCheckAuthProvider(
    appId: String,
    tokenClient: HttpClient?,
): AuthProvider = AndroidAppCheckAuthProvider()
