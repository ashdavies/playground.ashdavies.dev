package io.ashdavies.party.material

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
internal fun Modifier.padding(spacing: Spacing) = padding(
    horizontal = spacing.horizontal,
    vertical = spacing.vertical,
)
