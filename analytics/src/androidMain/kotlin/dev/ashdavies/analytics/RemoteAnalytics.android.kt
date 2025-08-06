package dev.ashdavies.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

public actual fun RemoteAnalytics(): RemoteAnalytics = RemoteAnalytics { name, block ->
    Firebase.analytics.logEvent(name) { block(ParametersBuilder(::param)) }
}
