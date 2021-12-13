package io.ashdavies.playground.common

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Suppress("UNCHECKED_CAST")
internal fun <T : ViewModel> Factory(create: () -> T) = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
}

@Composable
internal inline fun <reified T : ViewModel> viewModel(
    noinline create: () -> T
): T = viewModel(factory = Factory(create))
