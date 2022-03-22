package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

internal class PlaygroundRootComponent(
    componentContext: ComponentContext
) : PlaygroundRoot, ComponentContext by componentContext {

    private val router: Router<ChildConfiguration, PlaygroundRoot.Child> = router(
        initialConfiguration = ChildConfiguration.Events,
        childFactory = ::createChild,
        handleBackButton = true,
    )

    override val routerState: Value<RouterState<*, PlaygroundRoot.Child>>
        get() = router.state

    private fun createChild(
        configuration: ChildConfiguration,
        componentContext: ComponentContext,
    ): PlaygroundRoot.Child = when (configuration) {
        is ChildConfiguration.Events -> PlaygroundRoot.Child.Events(createNavigation(componentContext))
        is ChildConfiguration.Profile -> PlaygroundRoot.Child.Profile(createNavigation(componentContext))
    }

    private fun createNavigation(componentContext: ComponentContext): PlaygroundRoot.Navigation = NavigationComponent(
        navigateToProfile = { router.push(ChildConfiguration.Profile) },
        navigateToEvents = { router.push(ChildConfiguration.Events) },
        componentContext = componentContext,
    )
}

private class NavigationComponent(
    componentContext: ComponentContext,
    private val navigateToEvents: () -> Unit,
    private val navigateToProfile: () -> Unit
) : PlaygroundRoot.Navigation, ComponentContext by componentContext {
    override fun navigateToEvents() = navigateToEvents.invoke()
    override fun navigateToProfile() = navigateToProfile.invoke()
}

sealed class ChildConfiguration : Parcelable {
    @Parcelize object Events : ChildConfiguration()
    @Parcelize object Profile : ChildConfiguration()
}
