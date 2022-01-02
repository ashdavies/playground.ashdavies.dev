package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

class ComposableCompositionLocal<T>(private val factory: @Composable () -> T) {

    private val local = compositionLocalOf<T?> { null }
    val current: T @Composable get() = local.current ?: factory()

    infix fun provides(value: T?) = local.provides(value)
}

fun <T> composableCompositionLocalOf(factory: @Composable () -> T) =
    ComposableCompositionLocal(factory)
