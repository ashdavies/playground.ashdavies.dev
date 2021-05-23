package io.ashdavies.playground.store

import io.ashdavies.playground.store.Options.Limit
import io.ashdavies.playground.store.Options.Limit.Companion.Default

internal interface Options {
    val refresh: Boolean
    val orderBy: String?
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
    override val orderBy: String?,
    override val startAt: String?,
    override val limit: Limit,
) : Options

internal fun Options(
    refresh: Boolean = false,
    orderBy: String? = null,
    startAt: String? = null,
    limit: Limit = Default,
): Options = OptionsImpl(
    refresh = refresh,
    orderBy = orderBy,
    startAt = startAt,
    limit = limit,
)
