package io.ashdavies.lanyard.material

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
internal fun Modifier.padding(spacing: Spacing) = padding(spacing.values)
