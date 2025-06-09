package io.ashdavies.tally.events.paging

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ashdavies.config.RemoteConfig
import io.ashdavies.tally.PlaygroundDatabase
import io.ktor.client.HttpClient

@ContributesTo(AppScope::class)
internal interface EventPagerModule {

    @Provides
    fun eventPager(
        httpClient: HttpClient,
        remoteConfig: RemoteConfig,
        playgroundDatabase: PlaygroundDatabase,
    ): EventPager = EventPager(
        eventsCallable = PagedUpcomingEventsCallable(
            httpClient = httpClient,
            remoteConfig = remoteConfig,
        ),
        eventsQueries = playgroundDatabase.eventsQueries,
    )
}