package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.AppCheck
import dev.ashdavies.http.common.models.AppCheckToken
import dev.ashdavies.http.common.models.XFirebaseAppCheck
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.AuthScheme
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.utils.io.KtorDsl
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi

internal fun AuthConfig.appCheck(block: AppCheckAuthConfig.() -> Unit) {
    with(AppCheckAuthConfig().apply(block)) {
        this@appCheck.providers.add(AppCheckAuthProvider(getToken, realm))
    }
}

@KtorDsl
internal class AppCheckAuthConfig {
    var getToken: suspend () -> AppCheckToken? = { null }
    var realm: String? = null

    fun getToken(block: suspend () -> AppCheckToken?) {
        getToken = block
    }
}

@OptIn(ExperimentalAtomicApi::class)
internal class AppCheckAuthProvider(
    private val getToken: suspend () -> AppCheckToken?,
    private val realm: String?,
) : AuthProvider {

    private val holder = AtomicReference<AppCheckToken?>(null)
    private val mutex = Mutex()

    @Deprecated("Please use sendWithoutRequest function instead", level = DeprecationLevel.ERROR)
    override val sendWithoutRequest: Boolean
        get() = error("Deprecated")

    override fun sendWithoutRequest(request: HttpRequestBuilder): Boolean = true

    override fun isApplicable(auth: HttpAuthHeader): Boolean {
        if (auth.authScheme != AuthScheme.AppCheck) {
            println("AppCheck Auth Provider is not applicable for $auth")
            return false
        }

        val isSameRealm = when {
            realm == null -> true
            auth !is HttpAuthHeader.Parameterized -> false
            else -> auth.parameter("realm") == realm
        }

        if (!isSameRealm) {
            println("AppCheck Auth Provider is not applicable for this realm")
        }

        return isSameRealm
    }

    override suspend fun addRequestHeaders(
        request: HttpRequestBuilder,
        authHeader: HttpAuthHeader?,
    ) {
        val token = mutex.withLock {
            holder.load() ?: getToken().also(holder::store)
        } ?: return

        request.headers {
            if (contains(HttpHeaders.XFirebaseAppCheck)) {
                remove(HttpHeaders.XFirebaseAppCheck)
            }

            if (request.attributes.contains(AuthCircuitBreaker).not()) {
                append(HttpHeaders.XFirebaseAppCheck, token.token)
            }
        }
    }

    override suspend fun refreshToken(response: HttpResponse): Boolean = mutex.withLock {
        return getToken().also(holder::store) != null
    }

    override fun clearToken() {
        holder.store(null)
    }
}
