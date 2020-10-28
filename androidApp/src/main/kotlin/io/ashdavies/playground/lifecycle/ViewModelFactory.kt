@file:Suppress("FunctionName", "UNCHECKED_CAST")

package io.ashdavies.playground.lifecycle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import io.ashdavies.playground.Graph
import io.ashdavies.playground.graph

internal fun <T, VM : ViewModel> ViewModelFactory(
    input: T,
    block: Graph<T>.() -> VM,
): Factory = object : Factory {

    override fun <VM : ViewModel?> create(
        modelClass: Class<VM>
    ): VM = input
        .graph
        .block() as VM
}
