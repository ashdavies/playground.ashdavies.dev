package io.ashdavies.playground

inline fun <reified T> Any?.unsafeCast(): T = this as T
