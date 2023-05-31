package io.ashdavies.notion

import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient

public fun NotionClient(authentication: Authentication): NotionClient {
    return NotionClient.newInstance(ClientConfiguration(authentication))
}

public fun NotionClient(accessToken: String): NotionClient {
    return NotionClient(Authentication(accessToken))
}
