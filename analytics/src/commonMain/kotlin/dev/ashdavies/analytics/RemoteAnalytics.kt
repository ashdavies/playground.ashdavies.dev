package dev.ashdavies.analytics

public interface RemoteAnalytics {
    public fun logEvent(name: String, block: ParametersBuilder.() -> Unit = { })
    public fun recordException(throwable: Throwable)
}

public fun interface ParametersBuilder {
    public fun param(key: String, value: String)
}
