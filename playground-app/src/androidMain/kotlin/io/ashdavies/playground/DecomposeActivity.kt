package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.defaultComponentContext

public abstract class DecomposeActivity : ComposeActivity() {

    abstract val root: @Composable (ComponentContext) -> Unit

    final override val content = @Composable {
        PlaygroundScreen { root(defaultComponentContext()) }
    }
}
