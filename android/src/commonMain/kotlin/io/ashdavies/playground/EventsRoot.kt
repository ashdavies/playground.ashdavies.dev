package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import io.ashdavies.playground.events.EventsScreen
import io.ashdavies.playground.profile.ProfileScreen

@Composable
@OptIn(ExperimentalDecomposeApi::class)
internal fun EventsRoot(root: EventsRoot, modifier: Modifier = Modifier) {
    Children(root.routerState, modifier, childAnimation(slide())) {
        when (val child: EventsRoot.Child = it.instance) {
            is EventsRoot.Child.Events -> EventsScreen(child)
            is EventsRoot.Child.Profile -> ProfileScreen(child)
        }
    }
}

internal interface EventsRoot : NavigationRoot<EventsRoot.Navigation, EventsRoot.Child> {
    sealed interface Child : NavigationRoot.Child<Navigation> {
        data class Events(override val navigation: Navigation) : Child
        data class Profile(override val navigation: Navigation) : Child
    }

    interface Navigation : NavigationRoot.Navigation {
        fun navigateToEvents()
        fun navigateToProfile()
    }
}
