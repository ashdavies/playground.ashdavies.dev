package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide

@Composable
@OptIn(ExperimentalDecomposeApi::class)
internal fun DominionRoot(root: DominionRoot, modifier: Modifier = Modifier) {
    Children(root.routerState, modifier, childAnimation(slide())) {
        when (val child: DominionRoot.Child = it.instance) {
            is DominionRoot.Child.Details -> TODO()
            is DominionRoot.Child.List -> TODO()
        }
    }
}

internal interface DominionRoot : NavigationRoot<DominionRoot.Navigation, DominionRoot.Child> {
    sealed interface Child : NavigationRoot.Child<Navigation> {
        data class Details(override val navigation: Navigation) : Child
        data class List(override val navigation: Navigation) : Child
    }

    interface Navigation : NavigationRoot.Navigation {
        fun navigateToDetails()
        fun navigateToList()
    }
}
