package io.ashdavies.graphics

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
public actual fun AsyncImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier,
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}
