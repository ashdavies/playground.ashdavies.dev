package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

internal class EventsRootComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext,
    EventsRoot {

    private val router: Router<ChildConfiguration, EventsRoot.Child> = router(
        initialConfiguration = ChildConfiguration.Events,
        childFactory = ::createChild,
        handleBackButton = true,
    )

    override val routerState: Value<RouterState<*, EventsRoot.Child>>
        get() = router.state

    private fun createChild(
        configuration: ChildConfiguration,
        componentContext: ComponentContext,
    ): EventsRoot.Child = when (configuration) {
        is ChildConfiguration.Events -> EventsRoot.Child.Events(createNavigation(componentContext))
        is ChildConfiguration.Profile -> EventsRoot.Child.Profile(createNavigation(componentContext))
    }

    private fun createNavigation(componentContext: ComponentContext): EventsRoot.Navigation = NavigationComponent(
        navigateToProfile = { router.bringToFront(ChildConfiguration.Profile) },
        navigateToEvents = { router.bringToFront(ChildConfiguration.Events) },
        componentContext = componentContext,
    )
}

private class NavigationComponent(
    componentContext: ComponentContext,
    private val navigateToEvents: () -> Unit,
    private val navigateToProfile: () -> Unit,
) : ComponentContext by componentContext, EventsRoot.Navigation {
    override fun navigateToEvents() = navigateToEvents.invoke()
    override fun navigateToProfile() = navigateToProfile.invoke()
}

internal sealed class ChildConfiguration : Parcelable {
    @Parcelize object Events : ChildConfiguration()
    @Parcelize object Profile : ChildConfiguration()
}
