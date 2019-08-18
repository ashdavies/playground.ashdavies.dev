package io.ashdavies.databinding.extensions

internal inline fun <reified T> Any?.unsafeCast() = this as T
