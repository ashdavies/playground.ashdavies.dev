package io.ashdavies.cloud

import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode

public fun interface Identifier<T : Any> : (T) -> String

public inline fun <reified T : Any> Identifier(): Identifier<T> {
    val cache = mutableMapOf<T, String>()

    return Identifier { value ->
        cache.getOrPut(value) {
            Json
                .encodeToString(value)
                .encode()
                .md5()
                .hex()
        }
    }
}
