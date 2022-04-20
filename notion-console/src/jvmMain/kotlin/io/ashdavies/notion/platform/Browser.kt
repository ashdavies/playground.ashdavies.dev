package io.ashdavies.notion.platform

import java.awt.Desktop
import java.net.URI

public actual object Browser {

    private val desktop by lazy { Desktop.getDesktop() }

    public actual fun launch(uriString: String): Boolean {
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
