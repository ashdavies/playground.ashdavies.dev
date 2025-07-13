package io.ashdavies.tally.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.io.files.Path

@Composable
internal expect fun ImageCapture(
    onCapture: (Path?) -> Unit,
    modifier: Modifier = Modifier,
)
