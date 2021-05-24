package io.ashdavies.playground.store

internal interface Cache<Key : Any, Value : Any> {

    suspend fun read(key: Key, options: Options): Value?
    suspend fun write(key: Key, value: Value)

    suspend fun delete(key: Key) {
        throw UnsupportedOperationException()
    }

    suspend fun clear() {
        throw UnsupportedOperationException()
    }
}
