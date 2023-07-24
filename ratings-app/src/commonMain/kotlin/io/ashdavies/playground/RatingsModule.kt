package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import io.ashdavies.notion.getNotionHttpClient
import io.ktor.client.HttpClient

@Composable
internal fun rememberRatingsService(
    httpClient: HttpClient = rememberNotionHttpClient(),
): RatingsService = remember(httpClient) {
    RatingsService(httpClient)
}

@Composable
private fun rememberNotionHttpClient(
    accessTokenFileString: String = "${getFilesDir()}/tokens.db",
    uriHandler: UriHandler = LocalUriHandler.current,
): HttpClient = remember(uriHandler) {
    getNotionHttpClient(
        accessTokenFileString = accessTokenFileString,
        openUri = uriHandler::openUri,
    )
}
