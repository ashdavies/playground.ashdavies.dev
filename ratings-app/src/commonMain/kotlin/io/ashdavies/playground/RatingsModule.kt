package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import io.ashdavies.notion.notionHttpClient
import io.ktor.client.HttpClient

@Composable
internal fun rememberRatingsService(
    client: HttpClient = notionHttpClient,
): RatingsService = remember(client) {
    RatingsService(client)
}
