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


internal sealed class ChildConfiguration : Parcelable {
    @Parcelize object Events : ChildConfiguration()
    @Parcelize object Profile : ChildConfiguration()
}

private fun createNavigation(router: Router<ChildConfiguration, EventsRoot.Child>) = object : EventsRoot.Navigation {
    override fun navigateToEvents() = router.bringToFront(ChildConfiguration.Events)
    override fun navigateToProfile() = router.bringToFront(ChildConfiguration.Profile)
}
