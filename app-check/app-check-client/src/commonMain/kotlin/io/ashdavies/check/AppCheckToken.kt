package io.ashdavies.check

import androidx.compose.runtime.Composable
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

@Serializable
public data class AppCheckToken(
    public val expireTimeMillis: Long,
    public val token: String,
)

@Composable
public expect fun ProvideAppCheckToken(
    client: HttpClient/* = LocalHttpClient.current*/,
    content: @Composable () -> Unit,
)
