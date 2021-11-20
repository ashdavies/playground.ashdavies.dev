package io.ashdavies.notion

import io.ktor.client.HttpClient
import io.ktor.client.request.get

internal fun HttpNotionPages(client: HttpClient, baseUrl: String) = NotionPages { pageId ->
    client.get("$baseUrl/pages/$pageId") { }
}
