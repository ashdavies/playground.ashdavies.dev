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
        initialConfiguration = ChildConfiguration.Expansion,
        handleBackButton = true,
    )

    override val routerState: Value<RouterState<*, DominionRoot.Child>>
        get() = router.state

    private fun createChild(configuration: ChildConfiguration): DominionRoot.Child = when (configuration) {
        is ChildConfiguration.Expansion -> DominionRoot.Child.Expansion(createNavigation())
        is ChildConfiguration.Kingdom -> DominionRoot.Child.Kingdom(
            expansion = configuration.expansion,
            navigation = createNavigation(),
        )
        is ChildConfiguration.Card -> DominionRoot.Child.Card(createNavigation())
    }

    private fun createNavigation() = object : DominionRoot.Navigation {
        override fun navigateToExpansion() = router.bringToFront(ChildConfiguration.Expansion)
        override fun navigateToKingdom(expansion: DominionExpansion) {
            router.bringToFront(ChildConfiguration.Kingdom(expansion))
        }
    }
}

internal sealed class ChildConfiguration : Parcelable {

    @Parcelize
    object Expansion : ChildConfiguration()

    @Parcelize
    data class Kingdom(val expansion: DominionExpansion) : ChildConfiguration()

    @Parcelize
    object Card : ChildConfiguration()
}
