package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import io.ashdavies.dominion.DominionRoot

@Composable
internal fun DominionApp(componentContext: ComponentContext) {
    ComposeScreen { DominionRoot(componentContext) }
}
