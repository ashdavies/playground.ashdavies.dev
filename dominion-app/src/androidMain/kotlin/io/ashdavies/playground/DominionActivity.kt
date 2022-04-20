package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

internal class DominionActivity : DecomposeActivity() {
    override val root: @Composable (ComponentContext) -> Unit = {
        DominionRoot(DominionRootComponent(it))
    }
}
