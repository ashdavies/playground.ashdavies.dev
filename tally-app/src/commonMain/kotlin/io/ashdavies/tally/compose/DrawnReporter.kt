package io.ashdavies.tally.compose

import io.ashdavies.content.PlatformContext
import io.ashdavies.content.reportFullyDrawn

internal interface DrawnReporter {
    fun reportFullyDrawn()
}

internal fun DrawnReporter(platformContext: PlatformContext): () -> Unit {
    return platformContext::reportFullyDrawn
}
