package io.ashdavies.playground.store

internal interface Cache<Key : Any, Value : Any> {

    suspend fun read(key: Key, options: Options): Value?
    suspend fun write(key: Key, value: Value)
    suspend fun delete(key: Key)
    suspend fun clear()

    companion object {

        fun <Key : Any, Value : Any> Empty(): Cache<Key, Value> = Cache(
            read = { _, _ -> null },
            write = { _, _ -> },
            delete = { },
            clear = { }
        )
    }
}

internal fun <Key : Any, Value : Any> Cache(
    read: suspend (Key, Options) -> Value?,
    write: suspend (Key, Value) -> Unit,
    delete: suspend (Key) -> Unit,
    clear: suspend () -> Unit,
): Cache<Key, Value> = object : Cache<Key, Value> {

    override suspend fun read(key: Key, options: Options): Value? = read(key, options)
    override suspend fun write(key: Key, value: Value) = write(key, value)
    override suspend fun delete(key: Key) = delete(key)
    override suspend fun clear() = clear()
}
