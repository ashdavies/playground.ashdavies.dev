package io.ashdavies.tally

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import dev.ashdavies.http.defaultHttpClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ashdavies.content.PlatformContext
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header

@DependencyGraph(AppScope::class)
internal interface JvmTallyGraph : TallyGraph {

    @Provides
    fun httpClient(context: PlatformContext): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            header("User-Agent", System.getProperty("os.name"))
            header("X-API-Key", BuildConfig.BROWSER_API_KEY)
        }
    }

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(
            @Provides context: PlatformContext,
            @Provides windowSizeClass: WindowSizeClass,
        ): JvmTallyGraph
    }
}
