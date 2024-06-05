package io.ashdavies.sql

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import app.cash.sqldelight.Transacter

public val LocalTransacter: ProvidableCompositionLocal<Transacter> = compositionLocalOf {
    error("CompositionLocal LocalTransacter not present")
}

@Composable
public fun <T : Transacter> ProvideTransacter(value: T, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalTransacter provides value,
        content = content,
    )
}

@Composable
@Suppress("UNCHECKED_CAST")
public fun <T : Transacter, Q : Transacter> rememberQueries(
    transacter: T = LocalTransacter.current as T,
    transform: (T) -> Q,
): Q = remember(transacter) {
    transform(transacter)
}
