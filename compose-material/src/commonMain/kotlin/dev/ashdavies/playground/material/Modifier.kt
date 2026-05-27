package dev.ashdavies.playground.material

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
public fun Modifier.padding(spacing: Spacing): Modifier = padding(spacing.values)
