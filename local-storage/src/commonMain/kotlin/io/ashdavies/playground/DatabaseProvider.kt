package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf

public val LocalPlaygroundDatabase: ComposableCompositionLocal<PlaygroundDatabase> = composableCompositionLocalOf {
    DatabaseFactory(PlaygroundDatabase.Schema) { PlaygroundDatabase(it) }
}

public interface ComposableCompositionLocal<T> {
    public infix fun provides(value: T?): ProvidedValue<T?>
    @get:Composable public val current: T
}

private fun <T> composableCompositionLocalOf(factory: @Composable () -> T) = object : ComposableCompositionLocal<T> {

    private val local = compositionLocalOf<T?> { null }
    override val current: T @Composable get() = local.current ?: factory()

    override infix fun provides(value: T?): ProvidedValue<T?> = local.provides(value)
}
