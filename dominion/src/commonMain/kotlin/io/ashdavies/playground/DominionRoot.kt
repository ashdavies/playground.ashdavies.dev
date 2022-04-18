package io.ashdavies.playground

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.slide
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value

@Composable
@OptIn(ExperimentalDecomposeApi::class)
internal fun DominionRoot(root: DominionRoot, modifier: Modifier = Modifier) {
    DominionTheme {
        Children(root.routerState, modifier, childAnimation(slide())) {
            when (val child: DominionRoot.Child = it.instance) {
                is DominionRoot.Child.List -> TODO()
                is DominionRoot.Child.Details -> TODO()
            }
        }
    }
}

internal interface DominionRoot {

    val routerState: Value<RouterState<*, Child>>

    sealed class Child {

        abstract val navigation: Navigation

        data class List(override val navigation: Navigation) : Child()
        data class Details(override val navigation: Navigation) : Child()
    }

    interface Navigation {

        fun navigateToList()
        fun navigateToDetails()
    }
}
