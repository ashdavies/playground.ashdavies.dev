package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import io.ashdavies.playground.card.CardScreen
import io.ashdavies.playground.expansion.ExpansionScreen
import io.ashdavies.playground.kingdom.KingdomScreen

@Composable
@OptIn(ExperimentalDecomposeApi::class)
internal fun DominionRoot(root: DominionRoot, modifier: Modifier = Modifier) {
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
