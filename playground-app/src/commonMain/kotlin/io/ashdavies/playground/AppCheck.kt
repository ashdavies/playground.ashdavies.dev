package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.header
import io.ashdavies.http.path
import io.ashdavies.http.requestingState
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

private const val X_FIREBASE_APP_CHECK = "X-FIREBASE-AppCheck"

/**
 * TODO: Use property delegate to omit path("createToken") from request
 */
@Composable
public fun AppCheck(client: HttpClient = LocalHttpClient.current, content: @Composable () -> Unit) {
    val createToken by requestingState<AppCheckToken>(client) { path("createToken") }

    createToken.onSuccess {
        CompositionLocalProvider(
            LocalHttpClient provides client.header(X_FIREBASE_APP_CHECK, it),
            LocalAppCheckToken provides it,
            content = content,
        )
    }
}

@Serializable
public data class AppCheckToken(val token: String, val expireTimeMillis: Long)
