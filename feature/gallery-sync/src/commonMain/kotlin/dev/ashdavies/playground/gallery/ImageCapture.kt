package dev.ashdavies.playground.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun ImageCapture(
    onResult: (Path?) -> Unit,
    modifier: Modifier = Modifier,
)
