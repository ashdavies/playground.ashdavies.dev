package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext

@Composable
internal fun DominionScreen(componentContext: ComponentContext) {
    PlaygroundScreen {
        val root: DominionRoot = remember(componentContext) {
            DominionRootComponent(componentContext)
        }

        DominionRoot(root)
    }
}
