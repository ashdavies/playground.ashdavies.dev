package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf

public class ComposableCompositionLocal<T>(private val factory: @Composable () -> T) {

    private val local = compositionLocalOf<T?> { null }
    public val current: T @Composable get() = local.current ?: factory()

    public infix fun provides(value: T?): ProvidedValue<T?> = local.provides(value)
}

public fun <T> composableCompositionLocalOf(factory: @Composable () -> T): ComposableCompositionLocal<T> =
    ComposableCompositionLocal(factory)
