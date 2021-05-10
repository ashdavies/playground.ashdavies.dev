package io.ashdavies.playground.store

internal interface Fetcher<Key : Any, Value : Any> {

    suspend operator fun invoke(key: Key): Result<Value>
}

private class FetcherImpl<Key : Any, Value : Any>(
    private val block: suspend (Key) -> Value
) : Fetcher<Key, Value> {

    override suspend fun invoke(key: Key): Result<Value> {
        return runCatching { block(key) }
    }
}

internal fun <Key : Any, Value : Any> Fetcher(
    block: suspend (Key) -> Value
): Fetcher<Key, Value> = FetcherImpl(block)
