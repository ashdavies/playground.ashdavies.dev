package io.ashdavies.notion

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal fun HttpNotionSearch(client: HttpClient, baseUrl: String) = NotionSearch { query ->
    client.post("$baseUrl/search") {
        contentType(ContentType.Application.Json)
        body = query
    }
}
