package io.ashdavies.playground.android

import androidx.compose.runtime.Composable

@Composable
public actual fun FlowRow(content: @Composable () -> Unit) {
    com.google.accompanist.flowlayout.FlowRow(content = content)
}
