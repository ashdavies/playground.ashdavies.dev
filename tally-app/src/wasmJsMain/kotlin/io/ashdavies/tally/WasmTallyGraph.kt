package io.ashdavies.tally

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import io.ashdavies.tally.events.paging.EventPager

@DependencyGraph(AppScope::class)
internal interface WasmTallyGraph : TallyGraph {

    @Provides
    fun eventPager(): EventPager = object : EventPager {}

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides windowSize: WasmWindowSize,
        ): WasmTallyGraph
    }
}
