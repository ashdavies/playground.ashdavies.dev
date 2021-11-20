package io.ashdavies.notion

import java.awt.Desktop
import java.net.URI

actual object Browser {
    actual fun launch(uriString: String) {
        if (Desktop.isDesktopSupported() && desktop.isBrowseSupported) {
            desktop.browse(URI(uriString))
        }
    }
}

private val desktop: Desktop
    get() = Desktop.getDesktop()

private val Desktop.isBrowseSupported: Boolean
    get() = isSupported(Desktop.Action.BROWSE)
