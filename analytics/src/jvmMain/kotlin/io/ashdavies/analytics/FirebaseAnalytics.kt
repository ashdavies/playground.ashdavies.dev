package io.ashdavies.analytics

internal actual val firebaseAnalytics = RemoteAnalytics { name, parameters ->
    println("FirebaseAnalytics: $name, $parameters")
}
