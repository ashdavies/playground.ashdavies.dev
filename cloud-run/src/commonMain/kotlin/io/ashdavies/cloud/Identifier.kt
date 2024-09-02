package io.ashdavies.cloud

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encode

internal fun interface Identifier<T : Any> : (T) -> String

internal inline fun <reified T : Any> Identifier(
    cache: MutableMap<T, String> = mutableMapOf(),
) = Identifier<T> { value ->
    cache.getOrPut(value) {
        Json
            .encodeToString(value)
            .encode()
            .md5()
            .hex()
    }
}
