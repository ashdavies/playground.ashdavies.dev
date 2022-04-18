package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

internal class DominionRootComponent(
    componentContext: ComponentContext
) : DominionRoot, ComponentContext by componentContext {

    private val router: Router<ChildConfiguration, DominionRoot.Child> = router(
        initialConfiguration = ChildConfiguration.Events,
        childFactory = ::createChild,
        handleBackButton = true,
    )

    override val routerState: Value<RouterState<*, DominionRoot.Child>>
        get() = router.state

    private fun createChild(
        configuration: ChildConfiguration,
        componentContext: ComponentContext,
    ): DominionRoot.Child = when (configuration) {
        is ChildConfiguration.Events -> DominionRoot.Child.List(createNavigation(componentContext))
        is ChildConfiguration.Profile -> DominionRoot.Child.Details(createNavigation(componentContext))
    }

    private fun createNavigation(componentContext: ComponentContext): DominionRoot.Navigation = NavigationComponent(
        navigateToDetails = { router.bringToFront(ChildConfiguration.Profile) },
        navigateToList = { router.bringToFront(ChildConfiguration.Events) },
        componentContext = componentContext,
    )
}

private class NavigationComponent(
    componentContext: ComponentContext,
    private val navigateToList: () -> Unit,
    private val navigateToDetails: () -> Unit
) : DominionRoot.Navigation, ComponentContext by componentContext {
    override fun navigateToList() = navigateToList.invoke()
    override fun navigateToDetails() = navigateToDetails.invoke()
}

internal sealed class ChildConfiguration : Parcelable {
    @Parcelize object Events : ChildConfiguration()
    @Parcelize object Profile : ChildConfiguration()
}
