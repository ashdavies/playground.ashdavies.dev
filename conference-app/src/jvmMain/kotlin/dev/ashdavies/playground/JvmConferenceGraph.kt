package dev.ashdavies.playground

import dev.ashdavies.content.PlatformContext
import dev.ashdavies.http.defaultHttpClient
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header

@DependencyGraph(AppScope::class)
internal interface JvmConferenceGraph : ConferenceGraph {

    @Provides
    fun httpClient(context: PlatformContext): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            header("User-Agent", System.getProperty("os.name"))
            header("X-API-Key", BuildConfig.BROWSER_API_KEY)
        }
    }

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(@Provides context: PlatformContext): JvmConferenceGraph
    }
}
