package io.ashdavies.notion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

@Composable
internal fun rememberNotionClient(authentication: Authentication = Authentication()): NotionClient =
    remember(authentication) { NotionClient.newInstance(ClientConfiguration(authentication)) }
