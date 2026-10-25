package dev.ashdavies.playground.material

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
public inline fun Modifier.applyIf(predicate: Boolean, block: (Modifier) -> Modifier): Modifier = then(
    other = if (predicate) block(Modifier) else Modifier,
)

@Stable
public inline fun <T : Any> Modifier.applyIfNotNull(value: T?, block: (T) -> Modifier): Modifier = then(
    other = if (value != null) block(value) else Modifier,
)

@Stable
public fun Modifier.padding(spacing: Spacing): Modifier = padding(spacing.values)
