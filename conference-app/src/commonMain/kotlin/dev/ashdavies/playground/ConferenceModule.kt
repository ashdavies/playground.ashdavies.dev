package dev.ashdavies.playground

import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.content.PlatformContext
import dev.ashdavies.sql.DatabaseFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.ashdavies.playground.PlaygroundDatabase as ConferenceAppDatabase
import dev.ashdavies.playground.event.common.PlaygroundDatabase as EventCommonDatabase

@ContributesTo(AppScope::class)
internal interface ConferenceModule {

    @Provides
    fun playgroundDatabaseFactory(
        context: PlatformContext,
    ): DatabaseFactory<ConferenceAppDatabase> = DatabaseFactory(
        schema = ConferenceAppDatabase.Schema,
        context = context,
        factory = ConferenceAppDatabase::invoke,
    )

    @Provides
    fun eventDatabaseFactory(
        context: PlatformContext,
    ): DatabaseFactory<EventCommonDatabase> = DatabaseFactory(
        schema = EventCommonDatabase.Schema,
        context = context,
        factory = EventCommonDatabase::invoke,
    )

    @Provides
    fun remoteAnalytics(): RemoteAnalytics = RemoteAnalytics()
}
