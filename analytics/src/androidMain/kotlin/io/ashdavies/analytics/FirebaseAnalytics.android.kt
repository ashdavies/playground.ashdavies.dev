package io.ashdavies.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase

internal actual val firebaseAnalytics = RemoteAnalytics { name, block ->
    Firebase.analytics.logEvent(name) { block(ParametersBuilder(::param)) }
}
