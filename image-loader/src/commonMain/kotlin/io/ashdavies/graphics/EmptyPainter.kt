package io.ashdavies.graphics

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

public object EmptyPainter : Painter() {
    override val intrinsicSize: Size get() = Size.Unspecified
    override fun DrawScope.onDraw(): Unit = Unit
}
