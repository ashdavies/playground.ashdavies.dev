package io.ashdavies.notion

internal actual val NotionClientId: String
    get() = System.getenv("NOTION_CLIENT_ID")

internal actual val NotionClientSecret: String
    get() = System.getenv("NOTION_CLIENT_SECRET")
