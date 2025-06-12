package io.ashdavies.tally

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ashdavies.analytics.RemoteAnalytics
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.tally.gallery.PathProvider
import io.ashdavies.tally.gallery.StorageManager
import kotlinx.coroutines.Dispatchers

@ContributesTo(AppScope::class)
internal interface TallyModule {

    @Provides
    @SingleIn(AppScope::class)
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()

    @Provides
    @SingleIn(AppScope::class)
    fun remoteConfig(): RemoteConfig = RemoteConfig()

    @Provides
    @SingleIn(AppScope::class)
    fun storageManager(context: PlatformContext): StorageManager = StorageManager(
        platformContext = context,
        pathProvider = PathProvider(context),
        coroutineContext = Dispatchers.Default,
    )
}
