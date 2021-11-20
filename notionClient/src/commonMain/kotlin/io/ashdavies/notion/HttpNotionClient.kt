package io.ashdavies.notion

import io.ktor.client.HttpClient

private const val API_NOTION_V1 =
    "https://api.notion.com/v1"

internal class HttpNotionClient(
    private val client: HttpClient,
) : NotionClient,
    NotionAuth by HttpNotionAuth(client, API_NOTION_V1),
    NotionPages by HttpNotionPages(client, API_NOTION_V1),
    NotionSearch by HttpNotionSearch(client, API_NOTION_V1) {

    override fun close() {
        client.close()
    }
}
