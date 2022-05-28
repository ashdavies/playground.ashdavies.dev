package io.ashdavies.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext

public abstract class ComposeActivity(
    private val content: @Composable (ComponentContext) -> Unit,
    private val decorFitsSystemWindows: Boolean = false,
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, decorFitsSystemWindows)
        setContent { ComposeScreen { content(defaultComponentContext()) } }
    }
}
