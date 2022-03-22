package io.ashdavies.playground

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import io.ashdavies.playground.events.EventsScreen
import io.ashdavies.playground.profile.ProfileScreen

@Composable
@OptIn(ExperimentalDecomposeApi::class)
internal fun PlaygroundRoot(root: PlaygroundRoot) {
    PlaygroundTheme {
        Children(root.routerState, animation = slide()) {
            when (val child: PlaygroundRoot.Child = it.instance) {
                is PlaygroundRoot.Child.Events -> EventsScreen(child)
                is PlaygroundRoot.Child.Profile -> ProfileScreen(child)
            }
        }
    }
}

internal interface PlaygroundRoot {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {

        abstract val navigation: Navigation

        data class Events(override val navigation: Navigation) : Child()
        data class Profile(override val navigation: Navigation) : Child()
    }

    interface Navigation {

        fun navigateToEvents()
        fun navigateToProfile()
    }
}
