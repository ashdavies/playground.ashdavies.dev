package io.ashdavies.tally

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.sql.DatabaseFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.tally.gallery.imageAdapter

@ContributesTo(AppScope::class)
internal interface TallyModule {

    @Provides
    fun databaseFactory(context: PlatformContext): DatabaseFactory<PlaygroundDatabase> = DatabaseFactory(
        schema = PlaygroundDatabase.Schema,
        context = context,
        factory = { PlaygroundDatabase(it, imageAdapter()) },
    )

    @Provides
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()

    @Provides
    fun remoteConfig(): RemoteConfig = RemoteConfig()
}
