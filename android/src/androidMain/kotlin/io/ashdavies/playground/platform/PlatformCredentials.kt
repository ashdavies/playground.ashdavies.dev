package io.ashdavies.playground.platform

import io.ashdavies.playground.BuildConfig

public actual object PlatformCredentials {
    actual val googleClientId: String = BuildConfig.GOOGLE_CLIENT_ID
    actual val webApiKey: String = BuildConfig.PLAYGROUND_API_KEY
}
