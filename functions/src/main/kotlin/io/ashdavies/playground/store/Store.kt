package io.ashdavies.playground.store

internal interface Store<Key : Any, Value : Any> {

    suspend operator fun invoke(key: Key, options: Options): Result<Value>
}

private class StoreImpl<Key : Any, Value : Any>(
    private val fetcher: Fetcher<Key, Value>,
    private val cache: Cache<Key, Value>,
) : Store<Key, Value> {

    override suspend fun invoke(key: Key, options: Options): Result<Value> {
        if (options.refresh) {
            fetcher(key).onSuccess { cache.write(key, it) }
        }

        val cached: Value? = cache.read(key, options)
        if (cached != null) {
            return Result.success(cached)
        }

        return Result.failure(StoreError(404, "Not Found"))
    }
}

internal class StoreError(val code: Int, message: String) : Error(message)

internal fun <Key : Any, Value : Any> Store(
    fetcher: Fetcher<Key, Value>,
    cache: Cache<Key, Value>,
): Store<Key, Value> = StoreImpl(
    fetcher = fetcher,
    cache = cache,
)


