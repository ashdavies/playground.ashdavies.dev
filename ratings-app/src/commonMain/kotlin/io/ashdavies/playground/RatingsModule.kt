package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient

@Composable
internal fun rememberRatingsService(
    client: HttpClient = LocalHttpClient.current,
): RatingsService = remember(client) {
    RatingsService(client)
}
