package io.ashdavies.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase

public actual fun RemoteAnalytics(): RemoteAnalytics = RemoteAnalytics { name, block ->
    Firebase.analytics.logEvent(name) { block(ParametersBuilder(::param)) }
}
