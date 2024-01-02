package io.ashdavies.playground

import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource

internal actual object LauncherDrawableTokens {
    actual val afterParty: Any get() = useResource("drawable/after_party.png", ::loadImageBitmap)
    actual val dominion: Any get() = useResource("drawable/dominion.png", ::loadImageBitmap)
}
