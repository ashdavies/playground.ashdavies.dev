package io.ashdavies.playground.kotlin

public actual typealias Closeable = java.io.Closeable

public actual fun Closeable(close: () -> Unit): Closeable {
    return java.io.Closeable(close)
}
