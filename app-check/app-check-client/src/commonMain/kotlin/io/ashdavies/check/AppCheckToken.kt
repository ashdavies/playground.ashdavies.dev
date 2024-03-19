package io.ashdavies.check

import androidx.compose.runtime.Composable
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient

@Composable
public expect fun ProvideAppCheckToken(
    client: HttpClient = LocalHttpClient.current,
    content: @Composable () -> Unit,
)
