package io.ashdavies.notion.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

internal val LocalNotionClient = compositionLocalOf { NotionClient.newInstance(Authentication()) }

@Composable
internal fun ProvideNotionClient(authentication: Authentication, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalNotionClient provides NotionClient.newInstance(authentication),
        content = content
    )
}

private fun NotionClient.Companion.newInstance(authentication: Authentication): NotionClient {
    return newInstance(ClientConfiguration(authentication))
}
