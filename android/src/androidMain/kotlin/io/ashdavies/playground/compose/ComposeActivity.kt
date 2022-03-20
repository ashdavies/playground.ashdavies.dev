package io.ashdavies.playground.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows

internal abstract class ComposeActivity(private val decorFitsSystemWindows: Boolean = false) : ComponentActivity() {

    abstract val content: @Composable () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, decorFitsSystemWindows)
        setContent { content() }
    }
}
