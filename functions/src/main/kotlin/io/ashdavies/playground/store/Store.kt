package io.ashdavies.playground.store

internal interface Store<Key : Any, Value : Any> {

    suspend operator fun invoke(key: Key, options: Options): Result<Value>
}

private class StoreImpl<Key : Any, Value : Any>(
    private val fetcher: Fetcher<Key, Value>,
    private val cache: Cache<Key, Value>,
) : Store<Key, Value> {

    override suspend fun invoke(key: Key, options: Options): Result<Value> {
        return when (val cached: Value? =
            if (!options.refresh) cache.read(key, options) else null) {
            null -> fetcher(key).onSuccess { cache.write(key, it) }
            else -> Result.success(cached)
        }
    }
}

internal fun <Key : Any, Value : Any> Store(
    fetcher: Fetcher<Key, Value>,
    cache: Cache<Key, Value>,
): Store<Key, Value> = StoreImpl(
    fetcher = fetcher,
    cache = cache,
)


