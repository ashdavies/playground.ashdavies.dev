package io.ashdavies.tally.activity

public fun interface FullyDrawnReporter {

    public fun reportFullyDrawn()

    public companion object : FullyDrawnReporter {

        override fun reportFullyDrawn(): Unit = Unit
    }
}
