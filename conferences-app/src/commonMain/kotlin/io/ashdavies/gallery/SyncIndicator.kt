package io.ashdavies.gallery

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Color.Companion.LightGreen: Color
    get() = Color(0xFFA5FFA5)

private val Color.Companion.Orange: Color
    get() = Color(0xFFFFA500)

private const val ANIMATION_DURATION = 3_600
private const val ROTATION_SPEED = 10
private const val SYNCING_SCALE = 0.75f

private const val FULL_ROTATION = 360
private const val HALF_ROTATION = 180

@Composable
internal fun SyncIndicator(isSyncing: Boolean, modifier: Modifier = Modifier) {
    val tint by animateColorAsState(if (isSyncing) Color.Orange else Color.LightGreen)
    val scale by animateFloatAsState(if (isSyncing) SYNCING_SCALE else 1f)

    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(isSyncing) {
        if (isSyncing) {
            rotation.animateTo(
                targetValue = currentRotation + FULL_ROTATION,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = ANIMATION_DURATION,
                        easing = LinearEasing,
                    ),
                    repeatMode = RepeatMode.Restart,
                ),
                block = { currentRotation = value },
            )
        } else {
            val rotationRemaining = HALF_ROTATION - (currentRotation % HALF_ROTATION)
            val durationMillis = (rotationRemaining * ROTATION_SPEED).toInt()
            val targetValue = currentRotation + rotationRemaining

            rotation.animateTo(
                targetValue = targetValue,
                animationSpec = tween(
                    durationMillis = durationMillis,
                    easing = LinearOutSlowInEasing,
                ),
                block = { currentRotation = value },
            )
        }
    }

    Icon(
        imageVector = Icons.Filled.Sync,
        contentDescription = null,
        modifier = modifier
            .rotate(-rotation.value)
            .padding(4.dp)
            .scale(scale),
        tint = tint,
    )
}
