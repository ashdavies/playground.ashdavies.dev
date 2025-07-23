package io.ashdavies.tally.activity

import android.app.Activity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding

@ContributesBinding(AppScope::class, binding<FullyDrawnReporter>())
internal class AndroidFullyDrawnReporter @Inject constructor(private val activity: Activity) : FullyDrawnReporter {
    override fun reportFullyDrawn() = activity.reportFullyDrawn()
}
