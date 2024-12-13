package io.ashdavies.analytics

public fun interface RemoteAnalytics {
    public fun logEvent(name: String, block: ParametersBuilder.() -> Unit)
}

public fun interface ParametersBuilder {
    public fun param(key: String, value: String)
}
