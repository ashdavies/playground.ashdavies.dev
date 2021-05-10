package io.ashdavies.playground.store

import io.ashdavies.playground.store.Options.Limit

internal interface Options {
    val refresh: Boolean
    val startAt: String?
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
    override val startAt: String?,
    override val limit: Limit,
) : Options

internal fun Options(
    refresh: Boolean,
    startAt: String?,
    limit: Limit,
): Options = OptionsImpl(
    refresh = refresh,
    startAt = startAt,
    limit = limit,
)
