package dev.ashdavies.playground

import dev.ashdavies.content.PlatformContext
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.paging.Pager
import dev.ashdavies.playground.events.Event
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import kotlinx.browser.window

@DependencyGraph(AppScope::class)
internal interface WasmConferenceGraph : ConferenceGraph {

    @Provides
    fun httpClient(context: PlatformContext): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            header("User-Agent", window.navigator.userAgent)
            header("X-API-Key", BuildConfig.BROWSER_API_KEY)
        }
    }

    @Provides
    fun eventPager(): Pager<*, Event> = Pager<Any, _>()

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(@Provides context: PlatformContext): WasmConferenceGraph
    }
}
