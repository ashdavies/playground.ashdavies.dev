package io.ashdavies.playground.android

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope

public actual typealias ViewModel = androidx.lifecycle.ViewModel

public actual val ViewModel.viewModelScope: CoroutineScope
    get() = viewModelScope

@Composable
public actual inline fun <reified T : ViewModel> viewModel(noinline create: () -> T): T =
    viewModel(factory = Factory(create))

@PublishedApi
internal fun <T : ViewModel> Factory(create: () -> T) = object : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
}
