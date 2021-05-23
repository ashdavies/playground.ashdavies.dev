package io.ashdavies.playground.store

import io.ashdavies.playground.kotlin.requireNotNull

/**
 * Provided as a temporary alternative for dropbox/Store
 * https://github.com/dropbox/Store/issues/247
 */
internal interface Store<Key : Any, Value : Any> {
    suspend operator fun invoke(key: Key, options: Options): Result<Value>
}

private class StoreImpl<Key : Any, Value : Any>(
    private val fetcher: Fetcher<Key, Value>,
    private val cache: Cache<Key, Value>,
) : Store<Key, Value> {

    override suspend fun invoke(key: Key, options: Options): Result<Value> {
        val cached: Value? = cache.read(key, options)
        if (cached == null || options.refresh) {
            return fetcher(key)
                .onSuccess { cache.write(key, it) }
                .map { cache.read(key, options) }
                .requireNotNull { "Cache failure" }
        }

        return Result.success(cached)
    }
}

internal fun <Key : Any, Value : Any> Store(
    fetcher: Fetcher<Key, Value>,
    cache: Cache<Key, Value>,
): Store<Key, Value> = StoreImpl(
    fetcher = fetcher,
    cache = cache,
)


