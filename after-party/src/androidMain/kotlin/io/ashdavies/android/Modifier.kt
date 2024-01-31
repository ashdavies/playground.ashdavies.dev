package io.ashdavies.android

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder

public actual fun Modifier.fade(visible: Boolean): Modifier = composed {
    placeholder(visible, highlight = PlaceholderHighlight.fade())
}
