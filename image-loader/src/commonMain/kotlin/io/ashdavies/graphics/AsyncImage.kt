package io.ashdavies.graphics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
public expect fun AsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
)
