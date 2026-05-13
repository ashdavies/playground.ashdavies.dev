package dev.ashdavies.playground

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.playground.gallery.imageAdapter
import dev.ashdavies.sql.DatabaseFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.ashdavies.event.common.PlaygroundDatabase as EventDatabase

@ContributesTo(AppScope::class)
internal interface ConferenceModule {

    @Provides
    fun playgroundDatabaseFactory(
        context: PlatformContext,
    ): DatabaseFactory<PlaygroundDatabase> = DatabaseFactory(
        schema = PlaygroundDatabase.Schema,
        context = context,
        factory = { PlaygroundDatabase(it, imageAdapter()) },
    )

    @Provides
    fun eventDatabaseFactory(
        context: PlatformContext,
    ): DatabaseFactory<EventDatabase> = DatabaseFactory(
        schema = EventDatabase.Schema,
        context = context,
        factory = EventDatabase::invoke,
    )

    @Provides
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()

    @Provides
    fun remoteConfig(): RemoteConfig = RemoteConfig()
}
