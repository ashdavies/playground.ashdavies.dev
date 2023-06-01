package io.ashdavies.notion

import io.ktor.client.request.basicAuth
import io.ktor.http.HttpMethod
import io.ktor.server.auth.OAuthServerSettings

private const val DEFAULT_HOST = "https://api.notion.com/v1/oauth"

private val NotionClientId: String
    get() = requireNotNull(System.getenv("NOTION_CLIENT_ID"))

private val NotionClientSecret: String
    get() = requireNotNull(System.getenv("NOTION_CLIENT_SECRET"))

public object NotionSettings : () -> OAuthServerSettings {
    override fun invoke(): OAuthServerSettings = OAuthServerSettings.OAuth2ServerSettings(
        accessTokenInterceptor = { basicAuth(NotionClientId, NotionClientSecret) },
        authorizeUrl = "$DEFAULT_HOST/authorize",
        accessTokenUrl = "$DEFAULT_HOST/token",
        clientSecret = NotionClientSecret,
        requestMethod = HttpMethod.Post,
        clientId = NotionClientId,
        name = "notion",
    )
}
