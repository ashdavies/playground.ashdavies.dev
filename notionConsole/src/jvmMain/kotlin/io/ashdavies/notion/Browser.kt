package io.ashdavies.notion

import java.awt.Desktop
import java.net.URI

actual object Browser {

    private val desktop by lazy { Desktop.getDesktop() }

    actual fun launch(uriString: String): Boolean {
        if (!Desktop.isDesktopSupported()) {
            return false
        }

        if (!desktop.isSupported(Desktop.Action.BROWSE)) {
            return false
        }

        desktop.browse(URI(uriString))
        return true
    }
}
