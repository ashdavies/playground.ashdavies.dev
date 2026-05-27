package dev.ashdavies.analytics

public actual fun RemoteAnalytics(): RemoteAnalytics = RemoteAnalytics { name, parameters ->
    println("RemoteAnalytics: $name, $parameters")
}
