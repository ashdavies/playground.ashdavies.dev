package io.ashdavies.check

import androidx.compose.runtime.Composable
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

@Serializable
public data class AppCheckToken(public val token: String, public val expireTimeMillis: Long)

@Composable
public expect fun ProvideAppCheckToken(client: HttpClient, content: @Composable () -> Unit)

@Composable
public fun ProvideAppCheckToken(content: @Composable () -> Unit) {
    ProvideAppCheckToken(LocalHttpClient.current, content)
}
