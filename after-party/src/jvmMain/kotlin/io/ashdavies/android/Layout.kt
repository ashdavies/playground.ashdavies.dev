package io.ashdavies.android

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
public actual fun FlowRow(content: @Composable () -> Unit) {
    Row { content() }
}
