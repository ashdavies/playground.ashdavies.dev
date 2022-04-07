package io.ashdavies.playground

import io.ktor.server.auth.OAuthServerSettings

public object OAuthSettings : (OAuthProvider) -> OAuthServerSettings {
    override fun invoke(provider: OAuthProvider): OAuthServerSettings = when (provider) {
        is OAuthProvider.Google -> GoogleSettings()
    }
}
