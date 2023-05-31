package io.ashdavies.notion

import io.ktor.http.HttpMethod
import io.ktor.server.auth.OAuthServerSettings

private const val DEFAULT_HOST = "https://accounts.google.com/o/oauth2"

public object GoogleSettings : () -> OAuthServerSettings {
    override fun invoke(): OAuthServerSettings = OAuthServerSettings.OAuth2ServerSettings(
        clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
        defaultScopes = listOf(GoogleScope.USERINFO_PROFILE),
        clientId = System.getenv("GOOGLE_CLIENT_ID"),
        accessTokenUrl = "$DEFAULT_HOST/token",
        authorizeUrl = "$DEFAULT_HOST/auth",
        requestMethod = HttpMethod.Post,
        name = "google"
    )
}

internal object GoogleScope {
    private const val GOOGLE_AUTH = "https://www.googleapis.com/auth"
    internal const val USERINFO_PROFILE = "$GOOGLE_AUTH/userinfo.profile"
}
