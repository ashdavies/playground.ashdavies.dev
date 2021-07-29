package io.ashdavies.playground.kotlin

internal fun <T : Any> T?.requireNotNull(message: () -> Any): T =
    requireNotNull(this, message)

internal fun <T> T.Unit(block: () -> Any?): Unit =
    let { block(); Unit }
