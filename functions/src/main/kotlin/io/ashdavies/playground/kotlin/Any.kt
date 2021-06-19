package io.ashdavies.playground.kotlin

internal fun <T : Any> T?.requireNotNull(message: () -> Any): T {
    return requireNotNull(this, message)
}