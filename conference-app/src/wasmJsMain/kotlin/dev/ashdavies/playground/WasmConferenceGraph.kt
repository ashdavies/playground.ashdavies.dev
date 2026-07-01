package dev.ashdavies.playground

import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.firebase.rest.FirebaseRestRemoteConfig
import dev.ashdavies.content.PlatformContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.IntoSet
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import kotlinx.browser.window

@DependencyGraph(AppScope::class)
internal interface WasmConferenceGraph : ConferenceGraph {

    @IntoSet
    @Provides
    @Named("httpClientHeaders")
    fun httpClientHeaders(context: PlatformContext): Pair<String, String> {
        return "User-Agent" to window.navigator.userAgent
    }

    @Binds
    fun FirebaseRestRemoteConfig.remoteConfig(): RemoteConfig

    @DependencyGraph.Factory
    fun interface Factory {

        fun create(@Provides context: PlatformContext): WasmConferenceGraph
    }
}
