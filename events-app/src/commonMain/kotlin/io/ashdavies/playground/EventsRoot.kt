package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.ashdavies.check.ProvideAppCheckToken
import io.ashdavies.playground.events.EventsScreen
import io.ashdavies.playground.profile.ProfileScreen

@Composable
@OptIn(ExperimentalDecomposeApi::class)
public fun EventsRoot(componentContext: ComponentContext, modifier: Modifier = Modifier) {
    ProvideAppCheckToken { EventsRoot(rememberEventsRoot(componentContext), modifier) }
}

@Composable
@ExperimentalDecomposeApi
private fun EventsRoot(root: EventsRoot, modifier: Modifier = Modifier) {
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

private class EventsRootComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext,
    EventsRoot {

    private val router: Router<ChildConfiguration, EventsRoot.Child> = router(
        childFactory = { configuration, _ -> createChild(configuration) },
        initialConfiguration = ChildConfiguration.Events,
        handleBackButton = true,
    )

    override val routerState: Value<RouterState<*, EventsRoot.Child>>
        get() = router.state

    private fun createChild(configuration: ChildConfiguration): EventsRoot.Child = when (configuration) {
        is ChildConfiguration.Events -> EventsRoot.Child.Events(createNavigation(router))
        is ChildConfiguration.Profile -> EventsRoot.Child.Profile(createNavigation(router))
    }
}

private sealed class ChildConfiguration : Parcelable {
    @Parcelize
    object Events : ChildConfiguration()
    @Parcelize
    object Profile : ChildConfiguration()
}

private fun createNavigation(router: Router<ChildConfiguration, EventsRoot.Child>) = object : EventsRoot.Navigation {
    override fun navigateToEvents() = router.bringToFront(ChildConfiguration.Events)
    override fun navigateToProfile() = router.bringToFront(ChildConfiguration.Profile)
}

@Composable
private fun rememberEventsRoot(componentContext: ComponentContext): EventsRoot = remember {
    EventsRootComponent(componentContext)
}
