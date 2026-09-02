package dev.ashdavies.playground.material

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("UnusedReceiverParameter")
public val MaterialTheme.sizing: MaterialSizing
    get() = MaterialSizing

public object MaterialSizing {
    public val icon: Sizing = Sizing(
        small = 18.dp,
        medium = 32.dp,
        large = 72.dp,
    )
}

public data class Sizing(
    val small: Dp,
    val medium: Dp,
    val large: Dp,
)
