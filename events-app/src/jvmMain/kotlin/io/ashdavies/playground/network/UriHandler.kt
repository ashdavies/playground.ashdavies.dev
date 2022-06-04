package io.ashdavies.playground.network

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.Desktop
import java.awt.Desktop.isDesktopSupported
import java.net.URI

private val Desktop.isBrowseSupported: Boolean
    get() = isSupported(Desktop.Action.BROWSE)

@Composable
internal actual fun OpenUri(uri: String) {
    val desktop: Desktop = remember { Desktop.getDesktop() }
    if (isDesktopSupported() && desktop.isBrowseSupported) {
        desktop.browse(URI(uri))
    }
}
