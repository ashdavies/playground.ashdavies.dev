package io.ashdavies.playground.platform

public actual object PlatformCredentials {
    public actual val webApiKey: String = System.getenv("PLAYGROUND_API_KEY")
}
