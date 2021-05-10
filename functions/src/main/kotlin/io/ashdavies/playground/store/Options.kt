package io.ashdavies.playground.store

import io.ashdavies.playground.store.Options.Limit

internal interface Options {
    val refresh: Boolean
    val limit: Limit

    sealed class Limit {
        data class Limited(val value: Int) : Limit()
        object Unlimited : Limit()

        companion object {
            val Default: Limit get() = Limited(50)
        }
    }
}

private data class OptionsImpl(
    override val refresh: Boolean,
    override val limit: Limit,
) : Options

internal fun Options(
    refresh: Boolean,
    limit: Limit,
): Options = OptionsImpl(
    refresh = refresh,
    limit = limit,
)
