package io.ashdavies.playground.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf

class StaticComposableCompositionLocal<T>(private val factory: @Composable () -> T) {

    private val local = staticCompositionLocalOf<T?> { null }
    val current: T @Composable get() = local.current ?: factory()

    infix fun provides(value: T?): ProvidedValue<T?> {
        return local.provides(value)
    }
}

fun <T> staticComposableCompositionLocalOf(
    factory: @Composable () -> T
) = StaticComposableCompositionLocal(factory)
