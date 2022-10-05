package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext

@Composable
internal fun DominionApp(componentContext: ComponentContext) {
    ComposeScreen { DominionRoot(componentContext) }
}
