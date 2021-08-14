package io.ashdavies.playground.compose

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter

@ExperimentalCoilApi
internal val ImagePainter.isLoading: Boolean
    get() = state is ImagePainter.State.Loading

internal object EmptyPainter : Painter() {
    override val intrinsicSize: Size get() = Size.Unspecified
    override fun DrawScope.onDraw() = Unit
}
