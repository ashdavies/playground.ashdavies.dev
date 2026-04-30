package dev.ashdavies.playground.material

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
public val MaterialTheme.spacing: MaterialSpacing
    get() = MaterialSpacing

public val Spacing.values: PaddingValues
    get() = PaddingValues(
        horizontal = horizontal,
        vertical = vertical,
    )

public object MaterialSpacing {
    public val small: Spacing = Spacing(
        horizontal = 8.dp,
        vertical = 4.dp,
    )

    public val medium: Spacing = Spacing(
        horizontal = 12.dp,
        vertical = 8.dp,
    )

    public val large: Spacing = Spacing(
        horizontal = 16.dp,
        vertical = 12.dp,
    )
}

public data class Spacing(
    public val horizontal: Dp,
    public val vertical: Dp,
)
