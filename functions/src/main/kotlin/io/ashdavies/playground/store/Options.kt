package io.ashdavies.playground.store

import io.ashdavies.playground.store.Options.Limit
import io.ashdavies.playground.store.Options.Limit.Companion.Default

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

    companion object {
        val Default: Options = Options()
    }
}

private data class OptionsImpl(
    override val refresh: Boolean,
    override val startAt: String?,
    override val limit: Limit,
) : Options

internal fun Options(
    refresh: Boolean = false,
    startAt: String? = null,
    limit: Limit = Default,
): Options = OptionsImpl(
    refresh = refresh,
    startAt = startAt,
    limit = limit,
)
