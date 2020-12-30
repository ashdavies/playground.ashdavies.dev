package io.ashdavies.playground.common

import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.Window
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb

private val BlackScrim =
    Color(0f, 0f, 0f, 0.2f)

internal class SystemUi(private val window: Window) {

    fun setStatusBarColor(
        color: Color,
        darkIcons: Boolean = color.luminance() > 0.5f,
        transformColorForLightContent: (Color) -> Color = BlackScrim::compositeOver
    ) {
        with(window) {
            decorView.systemUiVisibility = when {
                darkIcons -> decorView.systemUiVisibility or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                else -> decorView.systemUiVisibility and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            statusBarColor = when {
                darkIcons -> transformColorForLightContent(color)
                else -> color
            }.toArgb()
        }
    }
}

internal val AmbientSystemUi = staticAmbientOf<SystemUi> {
    error("No SystemUiController provided")
}
