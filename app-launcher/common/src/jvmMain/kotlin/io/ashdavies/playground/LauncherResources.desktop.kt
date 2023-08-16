package io.ashdavies.playground

import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource

internal actual object LauncherDrawableTokens {
    actual val dominion: Any get() = useResource("drawable/dominion.png", ::loadImageBitmap)
    actual val gallery: Any get() = useResource("drawable/gallery.png", ::loadImageBitmap)
    actual val events: Any get() = useResource("drawable/events.png", ::loadImageBitmap)
    actual val ratings: Any get() = useResource("drawable/ratings.png", ::loadImageBitmap)
}
