package io.ashdavies.analytics

public fun interface RemoteAnalytics {
    public fun logEvent(name: String, parameters: Map<String, Any>?)
}
