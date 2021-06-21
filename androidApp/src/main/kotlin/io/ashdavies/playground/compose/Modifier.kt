package io.ashdavies.playground.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

internal fun Modifier.fade(visible: Boolean) = composed {
    Modifier.placeholder(
        highlight = PlaceholderHighlight.fade(),
        visible = visible,
    )
}