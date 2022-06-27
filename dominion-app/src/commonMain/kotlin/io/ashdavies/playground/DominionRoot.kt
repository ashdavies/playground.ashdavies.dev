package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.url
import io.ashdavies.playground.card.CardScreen
import io.ashdavies.playground.expansion.ExpansionScreen
import io.ashdavies.playground.kingdom.KingdomScreen
import io.ktor.http.takeFrom

/**
 * Dominion Strategy uses legacy wiki API without SSL with an API surface that is difficult to navigate.
 * Move necessary requests to Firebase Firestore and Cloud Functions.
 */
@Deprecated("dominionstrategy.com is deprecated")
private const val DOMINION_STRATEGY_HOST = "http://wiki.dominionstrategy.com/"

@Composable
@OptIn(ExperimentalDecomposeApi::class)
internal fun DominionRoot(componentContext: ComponentContext, modifier: Modifier = Modifier) {
    val client = LocalHttpClient.current.url { takeFrom(DOMINION_STRATEGY_HOST) }

    CompositionLocalProvider(LocalHttpClient provides client) {
        DominionRoot(rememberDominionRoot(componentContext), modifier)
    }
}

@Composable
@ExperimentalDecomposeApi
private fun DominionRoot(root: DominionRoot, modifier: Modifier = Modifier) {
    Children(root.routerState, modifier, childAnimation(slide())) {
        when (val child: DominionRoot.Child = it.instance) {
            is DominionRoot.Child.Expansion -> ExpansionScreen(child)
            is DominionRoot.Child.Kingdom -> KingdomScreen(child)
            is DominionRoot.Child.Card -> CardScreen(child)
        }
    }
}

internal interface DominionRoot : NavigationRoot<DominionRoot.Navigation, DominionRoot.Child> {
    sealed interface Child : Navigation, NavigationRoot.Child<Navigation> {
        data class Expansion(
            override val navigation: Navigation,
        ) : Child, Navigation by navigation

        data class Kingdom(
            override val navigation: Navigation,
            val expansion: DominionExpansion,
        ) : Child, Navigation by navigation

        data class Card(
            override val navigation: Navigation,
            val card: DominionCard,
        ) : Child, Navigation by navigation
    }

    interface Navigation : NavigationRoot.Navigation {
        fun navigateToKingdom(expansion: DominionExpansion)
        fun navigateToCard(card: DominionCard)
        fun navigateToExpansion()
    }
}

private class DominionRootComponent(componentContext: ComponentContext) :
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
            expansion = configuration.value,
            navigation = createNavigation(),
        )

        is ChildConfiguration.Card -> DominionRoot.Child.Card(
            navigation = createNavigation(),
            card = configuration.value,
        )
    }

    private fun createNavigation() = object : DominionRoot.Navigation {
        override fun navigateToExpansion() = router.bringToFront(ChildConfiguration.Expansion)
        override fun navigateToCard(card: DominionCard) = router.bringToFront(ChildConfiguration.Card(card))
        override fun navigateToKingdom(expansion: DominionExpansion) {
            router.bringToFront(ChildConfiguration.Kingdom(expansion))
        }
    }
}

private sealed class ChildConfiguration : Parcelable {
    @Parcelize object Expansion : ChildConfiguration()
    @Parcelize data class Kingdom(val value: DominionExpansion) : ChildConfiguration()
    @Parcelize data class Card(val value: DominionCard) : ChildConfiguration()
}

@Composable
private fun rememberDominionRoot(componentContext: ComponentContext): DominionRoot = remember {
    DominionRootComponent(componentContext)
}
