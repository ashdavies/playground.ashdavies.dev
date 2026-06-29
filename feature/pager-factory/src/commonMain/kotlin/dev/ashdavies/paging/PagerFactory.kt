package dev.ashdavies.paging

import androidx.paging.Pager

public fun interface PagerFactory<Key : Any, Value : Any> {
    public suspend fun create(config: PagerConfig<Key>): Pager<Key, Value>
}

public data class PagerConfig<Key : Any>(
    val initialKey: Key?,
    val pageSize: Int,
)
