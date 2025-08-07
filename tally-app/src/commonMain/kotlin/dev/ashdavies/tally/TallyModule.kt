package dev.ashdavies.tally

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.playground.PlaygroundDatabase
import dev.ashdavies.sql.DatabaseFactory
import dev.ashdavies.tally.gallery.imageAdapter
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

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
