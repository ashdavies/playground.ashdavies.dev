package dev.ashdavies.paging

import androidx.paging.Pager

public fun interface PagerFactory<Key : Any, Value : Any> {
    public suspend fun create(config: PagerConfig<Key>): Pager<Key, Value>
}

public data class PagerConfig<Key : Any>(
    val initialKey: Key? = null,
    val pageSize: Int = 10,
    val placeholderCount: Int = 6,
)
