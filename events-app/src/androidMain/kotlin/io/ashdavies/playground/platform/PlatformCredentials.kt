package io.ashdavies.playground.platform

import io.ashdavies.http.Environment
import io.ashdavies.http.require

public actual object PlatformCredentials {
    public actual val googleClientId: String = Environment.require("GOOGLE_CLIENT_ID")
    public actual val webApiKey: String = Environment.require("PLAYGROUND_API_KEY")
}
