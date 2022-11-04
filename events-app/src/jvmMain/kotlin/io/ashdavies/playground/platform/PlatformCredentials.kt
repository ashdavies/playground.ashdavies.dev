package io.ashdavies.playground.platform

public actual object PlatformCredentials {
    public actual val googleClientId: String = System.getenv("GOOGLE_CLIENT_ID")
    public actual val webApiKey: String = System.getenv("PLAYGROUND_API_KEY")
}
