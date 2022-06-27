package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.getValue
import io.ashdavies.http.header
import io.ashdavies.http.path
import io.ashdavies.http.requesting
import io.ktor.client.HttpClient

private const val X_FIREBASE_APP_CHECK = "X-FIREBASE-AppCheck"

@Composable
public fun AppCheck(client: HttpClient = LocalHttpClient.current, content: @Composable () -> Unit) {
    val token: AppCheckToken by requesting(client) { path("createToken") }

    CompositionLocalProvider(
        LocalHttpClient provides client.header(X_FIREBASE_APP_CHECK, token),
        LocalAppCheckToken provides token,
        content = content,
    )
}
