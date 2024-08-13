package io.ashdavies.cloud

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okio.ByteString.Companion.encode

internal interface Identifier<T : Any> : (T) -> String

internal class HashIdentifier<T : Any>(
    private val serializer: SerializationStrategy<T>,
) : Identifier<T> {

    private val cache = mutableMapOf<T, String>()

    override fun invoke(value: T): String = cache.getOrPut(value) {
        Json
            .encodeToString(serializer, value)
            .encode()
            .md5()
            .hex()
    }
}

internal inline fun <reified T : Any> Identifier(): Identifier<T> {
    return HashIdentifier(serializer())
}
