package io.ashdavies.notion.compose

import androidx.compose.runtime.compositionLocalOf
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

internal val LocalNotionClient = compositionLocalOf { NotionClient.newInstance(Authentication()) }

internal fun NotionClient.Companion.newInstance(authentication: Authentication): NotionClient {
    return newInstance(ClientConfiguration(authentication))
}
