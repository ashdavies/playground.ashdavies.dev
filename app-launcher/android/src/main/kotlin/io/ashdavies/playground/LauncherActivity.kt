package io.ashdavies.playground

import android.os.Parcelable
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.defaultComponentContext
import kotlinx.parcelize.Parcelize

@Parcelize
internal object AndroidLauncherScreen : LauncherScreen, Parcelable

internal class LauncherActivity : KotlinActivity({
    setDecorFitsSystemWindows(window, true)

    val circuitConfig = CircuitConfig(defaultComponentContext())
    val initialBackStack = buildInitialBackStack(
        nextScreen = intent.getStringExtra("route"),
        initialScreen = AndroidLauncherScreen,
    )

    setContent {
        LauncherApp(
            initialBackStack = initialBackStack,
            circuitConfig = circuitConfig,
        )
    }
})
