package dev.ashdavies.analytics

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@Inject
@ContributesBinding(AppScope::class)
public class LocalRemoteAnalytics : RemoteAnalytics {

    override fun logEvent(name: String, block: ParametersBuilder.() -> Unit) {
        println("RemoteAnalytics: $name, ${block(ParametersBuilder(mutableMapOf<String, String>()::put))}")
    }

    override fun recordException(throwable: Throwable) {
        throwable.printStackTrace()
    }
}
