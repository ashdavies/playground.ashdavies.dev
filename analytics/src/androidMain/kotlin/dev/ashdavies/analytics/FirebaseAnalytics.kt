package dev.ashdavies.analytics

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.crashlytics
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
internal class FirebaseAnalytics : RemoteAnalytics {
    override fun logEvent(name: String, block: ParametersBuilder.() -> Unit) {
        Firebase.analytics.logEvent(name) { block(ParametersBuilder(::param)) }
    }

    override fun recordException(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
    }
}
