package io.ashdavies.playground.android

import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel()

expect val ViewModel.viewModelScope: CoroutineScope

@Composable
expect inline fun <reified T : ViewModel> viewModel(
    noinline create: () -> T
): T
