@file:Suppress("UNCHECKED_CAST")

package io.ashdavies.playground.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import io.ashdavies.playground.Graph
import io.ashdavies.playground.graph

@Composable
internal inline fun <T, reified VM : ViewModel> T.graphViewModel(
    crossinline factory: Graph<T>.() -> VM,
): VM = viewModel(factory = object : Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T = this@graphViewModel
        .graph
        .factory() as T
})
