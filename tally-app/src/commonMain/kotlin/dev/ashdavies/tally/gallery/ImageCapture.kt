package dev.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.ashdavies.tally.files.Path

@Composable
internal expect fun ImageCapture(
    onResult: (Path?) -> Unit,
    modifier: Modifier = Modifier,
)
