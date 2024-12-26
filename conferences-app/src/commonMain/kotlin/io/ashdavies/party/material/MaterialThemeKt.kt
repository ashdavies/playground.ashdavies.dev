package io.ashdavies.party.material

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
internal val MaterialTheme.spacing: MaterialSpacing
    get() = MaterialSpacing

internal object MaterialSpacing {
    val small = Spacing(
        horizontal = 8.dp,
        vertical = 4.dp,
    )

    val medium = Spacing(
        horizontal = 12.dp,
        vertical = 8.dp,
    )

    val large = Spacing(
        horizontal = 16.dp,
        vertical = 12.dp,
    )
}

internal data class Spacing(
    val horizontal: Dp,
    val vertical: Dp,
)
