package io.ashdavies.tally

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.config.RemoteConfig

@ContributesTo(AppScope::class)
internal interface TallyModule {

    @Provides
    @SingleIn(AppScope::class)
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()

    @Provides
    @SingleIn(AppScope::class)
    fun remoteConfig(): RemoteConfig = RemoteConfig()
}
