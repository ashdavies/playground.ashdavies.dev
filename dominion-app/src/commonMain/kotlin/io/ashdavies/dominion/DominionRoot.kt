package io.ashdavies.dominion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.ashdavies.dominion.card.CardScreen
import io.ashdavies.dominion.expansion.ExpansionScreen
import io.ashdavies.dominion.kingdom.KingdomScreen
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.url
import io.ashdavies.playground.NavigationRoot
import io.ktor.http.takeFrom

/**
 * Dominion Strategy uses legacy wiki API without SSL with an API surface that is difficult to navigate.
 * Move necessary requests to Firebase Firestore and Cloud Functions.
 */
@Deprecated("dominionstrategy.com is deprecated")
private const val DOMINION_STRATEGY_HOST = "http://wiki.dominionstrategy.com/"

@Composable
@OptIn(ExperimentalDecomposeApi::class)
public fun DominionRoot(componentContext: ComponentContext, modifier: Modifier = Modifier) {
    val client = LocalHttpClient.current.url { takeFrom(DOMINION_STRATEGY_HOST) }

    CompositionLocalProvider(LocalHttpClient provides client) {
        DominionRoot(rememberDominionRoot(componentContext), modifier)
    }
}

@Composable
@ExperimentalDecomposeApi
private fun DominionRoot(root: DominionRoot, modifier: Modifier = Modifier) {
    Children(root.childStack, modifier, stackAnimation(slide())) {
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

    private val navigation = StackNavigation<ChildConfiguration>()

    private val _childStack = childStack(
        childFactory = { configuration, _ -> createChild(configuration) },
        initialConfiguration = ChildConfiguration.Expansion,
        handleBackButton = true,
        source = navigation,
    )

    override val childStack by ::_childStack

    private fun createChild(configuration: ChildConfiguration): DominionRoot.Child =
        when (configuration) {
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
        override fun navigateToExpansion() = navigation.bringToFront(ChildConfiguration.Expansion)
        override fun navigateToCard(card: DominionCard) =
            navigation.bringToFront(ChildConfiguration.Card(card))

        override fun navigateToKingdom(expansion: DominionExpansion) {
            navigation.bringToFront(ChildConfiguration.Kingdom(expansion))
        }
    }
}

private sealed class ChildConfiguration : Parcelable {

    @Parcelize
    object Expansion : ChildConfiguration()

    @Parcelize
    data class Kingdom(val value: DominionExpansion) : ChildConfiguration()

    @Parcelize
    data class Card(val value: DominionCard) : ChildConfiguration()
}

@Composable
private fun rememberDominionRoot(componentContext: ComponentContext): DominionRoot = remember {
    DominionRootComponent(componentContext)
}
