package io.ashdavies.playground.android

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing

public actual abstract class ViewModel

public actual val ViewModel.viewModelScope: CoroutineScope
    get() = CoroutineScope(Dispatchers.Swing)

@Composable
public actual inline fun <reified T : ViewModel> viewModel(
    noinline create: () -> T
): T = create()
