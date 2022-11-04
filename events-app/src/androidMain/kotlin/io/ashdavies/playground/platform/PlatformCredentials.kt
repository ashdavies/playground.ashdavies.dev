package io.ashdavies.playground.platform

import io.ashdavies.events.BuildConfig

public actual object PlatformCredentials {
    public actual val googleClientId: String = BuildConfig.GOOGLE_CLIENT_ID
    public actual val webApiKey: String = BuildConfig.PLAYGROUND_API_KEY
}
