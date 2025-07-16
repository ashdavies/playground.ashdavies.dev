package io.ashdavies.tally

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ashdavies.config.RemoteConfig
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.paging.Pager
import io.ashdavies.sql.DatabaseFactory
import io.ashdavies.sql.map
import io.ashdavies.tally.events.Event
import io.ashdavies.tally.events.paging.UpcomingEventsCallable
import io.ashdavies.tally.events.paging.eventPager
import io.ashdavies.tally.gallery.imageAdapter
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header

@DependencyGraph(AppScope::class)
internal interface JvmTallyGraph : TallyGraph {

    @Provides
    fun eventPager(
        httpClient: HttpClient,
        remoteConfig: RemoteConfig,
        databaseFactory: DatabaseFactory<PlaygroundDatabase>,
    ): Pager<*, Event> = eventPager(
        eventsCallable = UpcomingEventsCallable(
            httpClient = httpClient,
            remoteConfig = remoteConfig,
        ),
        eventsQueries = databaseFactory.map {
            it.eventsQueries
        },
    )

    @Provides
    fun httpClient(context: PlatformContext): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            header("User-Agent", System.getProperty("os.name"))
            header("X-API-Key", BuildConfig.BROWSER_API_KEY)
        }

        install(HttpCache) {
            publicStorage(context.resolveCacheDir())
        }
    }

    @Provides
    fun databaseFactory(context: PlatformContext): DatabaseFactory<PlaygroundDatabase> = DatabaseFactory(
        schema = PlaygroundDatabase.Schema,
        context = context,
        factory = { PlaygroundDatabase(it, imageAdapter()) },
    )

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(
            @Provides context: PlatformContext,
            @Provides windowSizeClass: WindowSizeClass,
        ): JvmTallyGraph
    }
}
