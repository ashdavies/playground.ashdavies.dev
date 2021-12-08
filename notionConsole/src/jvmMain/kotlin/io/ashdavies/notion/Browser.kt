package io.ashdavies.notion

import java.awt.Desktop
import java.net.URI

private val desktop: Desktop
    get() = Desktop.getDesktop()

private val Desktop.isBrowseSupported: Boolean
    get() = isSupported(Desktop.Action.BROWSE)

actual object Browser {
    actual fun launch(uriString: String) {
        if (Desktop.isDesktopSupported() && desktop.isBrowseSupported) {
            desktop.browse(URI(uriString))
        }
    }
}
