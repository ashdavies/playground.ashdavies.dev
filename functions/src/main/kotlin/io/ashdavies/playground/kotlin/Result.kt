package io.ashdavies.playground.kotlin

internal fun <T : Any> Result<T?>.requireNotNull(message: () -> String): Result<T> {
    return mapCatching { requireNotNull(it, message) }
}
