package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf

@Suppress("NO_EXPLICIT_RETURN_TYPE_IN_API_MODE_WARNING")
public val LocalPlaygroundDatabase = ComposableCompositionLocal {
    DatabaseFactory(
        factory = PlaygroundDatabase::invoke,
        schema = PlaygroundDatabase.Schema,
        config = getDriverConfig(),
    )
}

public class ComposableCompositionLocal<T>(private val factory: @Composable () -> T) {

    private val local = compositionLocalOf<T?> { null }

    public val current: T @Composable get() = local.current ?: factory()

    public infix fun provides(value: T?): ProvidedValue<T?> {
        return local.provides(value)
    }
}
