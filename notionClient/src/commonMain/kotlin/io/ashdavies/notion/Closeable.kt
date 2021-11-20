package io.ashdavies.notion

expect fun interface Closeable {
    fun close()
}

expect inline fun <T : Closeable, R> T.use(body: (T) -> R): R
