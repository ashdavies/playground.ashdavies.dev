package io.ashdavies.playground.platform

public actual object PlatformCredentials {
    actual val serverClientId: String = System.getenv("SERVER_CLIENT_ID")
    actual val webApiKey: String = System.getenv("PLAYGROUND_API_KEY")
}
