package io.ashdavies.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

internal actual val firebaseAnalytics = RemoteAnalytics { name, parameters ->
    Firebase.analytics.logEvent(name, parameters)
}
