package io.ashdavies.placeholder

import androidx.annotation.FloatRange
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

/**
 * A class which provides a brush to paint placeholder based on progress.
 */
@Stable
public interface PlaceholderHighlight {
    /**
     * The optional [AnimationSpec] to use when running the animation for this highlight.
     */
    public val animationSpec: InfiniteRepeatableSpec<Float>?

    /**
     * Return a [Brush] to draw for the given [progress] and [size].
     *
     * @param progress the current animated progress in the range of 0f..1f.
     * @param size The size of the current layout to draw in.
     */
    public fun brush(
        @FloatRange(from = 0.0, to = 1.0) progress: Float,
        size: Size,
    ): Brush

    /**
     * Return the desired alpha value used for drawing the [Brush] returned from [brush].
     *
     * @param progress the current animated progress in the range of 0f..1f.
     */
    @FloatRange(from = 0.0, to = 1.0)
    public fun alpha(progress: Float): Float

    public companion object
}

/**
 * Creates a [Fade] brush with the given initial and target colors.
 *
 * @param highlightColor the color of the highlight which is faded in/out.
 * @param animationSpec the [AnimationSpec] to configure the animation.
 */
public fun PlaceholderHighlight.Companion.fade(
    highlightColor: Color,
    animationSpec: InfiniteRepeatableSpec<Float> = PlaceholderDefaults.fadeAnimationSpec,
): PlaceholderHighlight = Fade(
    highlightColor = highlightColor,
    animationSpec = animationSpec,
)

private data class Fade(
    private val highlightColor: Color,
    override val animationSpec: InfiniteRepeatableSpec<Float>,
) : PlaceholderHighlight {
    private val brush = SolidColor(highlightColor)

    override fun brush(progress: Float, size: Size): Brush = brush
    override fun alpha(progress: Float): Float = progress
}
