package io.ashdavies.playground

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.System
import io.ashdavies.http.header
import io.ashdavies.http.onFailure
import io.ashdavies.http.onLoading
import io.ashdavies.http.path
import io.ashdavies.http.requestingState
import io.ashdavies.http.require
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

private const val X_FIREBASE_APP_CHECK = "X-Firebase-AppCheck"

/**
 * TODO: Use property delegate to omit path("createToken") from request
 */
@Composable
public fun AppCheck(client: HttpClient = LocalHttpClient.current, content: @Composable () -> Unit) {
    val createToken by requestingState<AppCheckToken>(client) {
        url.parameters.append("token", System.require("APP_CHECK_TOKEN"))
        path("createToken")
    }

    createToken.onSuccess {
        CompositionLocalProvider(
            LocalHttpClient provides client.header(X_FIREBASE_APP_CHECK, it),
            LocalAppCheckToken provides it,
            content = content,
        )
    }

    createToken.onLoading {
        Text("Loading... ($it / %)")

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = maxOf(0.05f, it),
        )
    }

    createToken.onFailure {
        Text(it.message ?: "Unknown error")
    }
}

@Serializable
public data class AppCheckToken(val token: String, val expireTimeMillis: Long)
