package io.ashdavies.notion

interface NotionClient :
    NotionAuth,
    NotionPages,
    NotionSearch,
    Closeable

fun interface NotionAuth {
    suspend fun auth(clientId: String, clientSecret: String): AuthResponse
}

fun interface NotionPages {
    suspend fun view(pageId: String): NotionPage<NotionObject>
}

fun interface NotionSearch {
    suspend fun search(query: SearchQuery): NotionPage<NotionObject>
}

fun NotionClient(logLevel: LogLevel = LogLevel.NONE): NotionClient =
    HttpNotionClient(HttpClientFactory.create(logLevel))

fun NotionClient(token: String, logLevel: LogLevel = LogLevel.NONE): NotionClient =
    HttpNotionClient(HttpClientFactory.create(token, logLevel))
