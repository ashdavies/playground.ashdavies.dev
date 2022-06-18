package io.ashdavies.check

internal inline fun <R, reified T : Throwable> runCatching(block: () -> R, transform: (T) -> Nothing): R {
    return runCatching(block) catch transform
}

internal inline infix fun <R, reified T : Throwable> Result<R>.catch(transform: (T) -> Nothing): R {
    return fold({ it }) { if (it is T) transform(it) else throw it }
}
