package io.ashdavies.tally

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ashdavies.content.PlatformContext
import io.ashdavies.http.defaultHttpClient
import io.ashdavies.paging.Pager
import io.ashdavies.tally.activity.FullyDrawnReporter
import io.ashdavies.tally.events.Event
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import kotlinx.browser.window

@DependencyGraph(AppScope::class)
internal interface WasmTallyGraph : TallyGraph {

    @Provides
    fun httpClient(context: PlatformContext): HttpClient = defaultHttpClient {
        install(DefaultRequest) {
            header("User-Agent", window.navigator.userAgent)
            header("X-API-Key", BuildConfig.BROWSER_API_KEY)
        }
    }

    @Provides
    fun eventPager(): Pager<*, Event> = Pager<Any, _>()

    @Provides
    fun fullyDrawnReporter(): FullyDrawnReporter = FullyDrawnReporter

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(
            @Provides context: PlatformContext,
            @Provides windowSizeClass: WindowSizeClass,
        ): WasmTallyGraph
    }
}
