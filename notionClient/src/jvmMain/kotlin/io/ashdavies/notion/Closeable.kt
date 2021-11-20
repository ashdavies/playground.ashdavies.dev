package io.ashdavies.notion

import java.io.Closeable
import kotlin.io.use

actual typealias Closeable = Closeable

actual inline fun <T : Closeable, R> T.use(body: (T) -> R): R = use(body)
