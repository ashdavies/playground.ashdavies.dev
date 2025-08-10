package dev.ashdavies.tally.activity

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding

@Inject
@ContributesBinding(AppScope::class, binding<FullyDrawnReporter>())
internal class NonAndroidFullyDrawnReporter : FullyDrawnReporter {
    override fun reportFullyDrawn() = Unit
}
