package io.ashdavies.dominion.layout

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides.Companion.Horizontal
import androidx.compose.foundation.layout.WindowInsetsSides.Companion.Top
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

public actual fun Modifier.windowInsetsPadding(): Modifier = composed {
    windowInsetsPadding(WindowInsets.systemBars.only(Horizontal + Top))
}
