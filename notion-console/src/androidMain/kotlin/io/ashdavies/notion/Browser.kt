package io.ashdavies.notion

import android.content.Intent
import android.net.Uri

public actual object Browser {
    actual fun launch(uriString: String): Boolean {
        Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
        return false
    }
}
