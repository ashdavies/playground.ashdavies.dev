package io.ashdavies.playground.platform

import io.ashdavies.playground.BuildConfig

public actual object PlatformCredentials {
    actual val serverClientId: String = BuildConfig.SERVER_CLIENT_ID
    actual val webApiKey: String = BuildConfig.PLAYGROUND_API_KEY
}
