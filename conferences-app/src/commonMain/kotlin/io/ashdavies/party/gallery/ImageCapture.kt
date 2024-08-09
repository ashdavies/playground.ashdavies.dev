package io.ashdavies.party.gallery

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal expect fun ImageCapture(
    manager: StorageManager,
    modifier: Modifier = Modifier,
    onCapture: (File?) -> Unit,
)
