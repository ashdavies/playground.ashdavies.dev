package io.ashdavies.playground.android

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

public actual abstract class ViewModel

public actual val ViewModel.viewModelScope: CoroutineScope
    get() = CoroutineScope(Dispatchers.Main)

@Composable
public actual inline fun <reified T : ViewModel> viewModel(
    noinline create: () -> T
): T = create()
