package io.ashdavies.playground

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.Router
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.bringToFront
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

internal class DominionRootComponent(componentContext: ComponentContext) :
    ComponentContext by componentContext,
    DominionRoot {

    private val router: Router<ChildConfiguration, DominionRoot.Child> = router(
        childFactory = { configuration, _ -> createChild(configuration) },
        initialConfiguration = ChildConfiguration.Listings,
        handleBackButton = true,
    )

    override val routerState: Value<RouterState<*, DominionRoot.Child>>
        get() = router.state

    private fun createChild(configuration: ChildConfiguration): DominionRoot.Child = when (configuration) {
        is ChildConfiguration.Listings -> DominionRoot.Child.List(createNavigation(router))
        is ChildConfiguration.Details -> DominionRoot.Child.Details(createNavigation(router))
    }
}

private fun createNavigation(router: Router<ChildConfiguration, DominionRoot.Child>) = object : DominionRoot.Navigation {
    override fun navigateToDetails() = router.bringToFront(ChildConfiguration.Details)
    override fun navigateToList() = router.bringToFront(ChildConfiguration.Listings)
}

internal sealed class ChildConfiguration : Parcelable {
    @Parcelize object Listings : ChildConfiguration()
    @Parcelize object Details : ChildConfiguration()
}
