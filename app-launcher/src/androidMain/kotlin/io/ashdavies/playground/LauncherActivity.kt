package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.defaultComponentContext

internal class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, true)

        setContent {
            val configuration = LocalConfiguration.current
            LauncherScreen(defaultComponentContext())
        }
    }
}
