package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.defaultComponentContext

private typealias OnCreateAction = ComponentActivity.(savedInstanceState: Bundle?) -> Unit

internal class LauncherActivity : KotlinActivity({
    setDecorFitsSystemWindows(window, true)
    setContent { LauncherScreen(defaultComponentContext()) }
})

internal abstract class KotlinActivity(private val action: OnCreateAction) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        action(savedInstanceState)
    }
}
