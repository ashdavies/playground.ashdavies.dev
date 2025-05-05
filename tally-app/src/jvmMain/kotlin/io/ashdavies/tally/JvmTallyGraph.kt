package io.ashdavies.tally

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ashdavies.sql.DatabaseFactory
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header

@DependencyGraph(AppScope::class)
internal interface JvmTallyGraph : TallyGraph {

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
    fun playgroundDatabase(context: PlatformContext): PlaygroundDatabase = DatabaseFactory(
        schema = PlaygroundDatabase.Schema,
        context = context,
        factory = { PlaygroundDatabase(it) },
    )

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(
            @Provides context: PlatformContext,
            @Provides windowSizeClass: WindowSizeClass,
        ): JvmTallyGraph
    }
}
