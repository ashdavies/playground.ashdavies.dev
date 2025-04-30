package io.ashdavies.routes

import com.slack.circuit.foundation.Circuit
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraphFactory
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.http.publicStorage
import io.ashdavies.io.resolveCacheDir
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.request.header

@DependencyGraph(AppScope::class)
internal interface RoutesGraph {

    val circuit: Circuit

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(@Provides context: PlatformContext): RoutesGraph
    }
}

@ContributesTo(AppScope::class)
internal interface RoutesModule {

    @Provides
    @SingleIn(AppScope::class)
    fun locationService(context: PlatformContext): LocationService = LocationService(context)

    @Provides
    @SingleIn(AppScope::class)
    fun httpClient(context: PlatformContext): HttpClient = defaultHttpClient {
        installHttpCache(context)
        installUserAgent()
    }
}

internal fun createRoutesGraph(context: PlatformContext): RoutesGraph {
    val factory = createGraphFactory<RoutesGraph.Factory>()
    return factory.create(context)
}

private fun HttpClientConfig<*>.installHttpCache(context: PlatformContext) {
    install(HttpCache) { publicStorage(context.resolveCacheDir()) }
}

private fun HttpClientConfig<*>.installUserAgent() {
    install(DefaultRequest) {
        header("User-Agent", System.getProperty("os.name"))
        // header("X-API-Key", BuildConfig.BROWSER_API_KEY)
    }
}
