package io.ashdavies.playground.network

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler

@Composable
internal actual fun OpenUri(uri: String) {
    LocalUriHandler.current.openUri(uri)
}
