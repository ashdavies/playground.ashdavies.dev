package io.ashdavies.playground.android

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

public expect abstract class ViewModel()

public expect val ViewModel.viewModelScope: CoroutineScope

@Composable
public expect inline fun <reified T : ViewModel> viewModel(
    noinline create: () -> T
): T
