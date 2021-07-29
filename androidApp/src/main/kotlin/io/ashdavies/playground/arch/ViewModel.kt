package io.ashdavies.playground.arch

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.getOrDefault
import androidx.lifecycle.viewmodel.compose.viewModel

private const val VIEW_EVENT_STORE = "viewEventStore"
private const val VIEW_STATE_STORE = "viewStateStore"

internal fun <T : ViewEvent> ViewModel.getViewEventStore(): ViewEventStore<T> =
    getOrDefault(VIEW_EVENT_STORE) { ViewEventStore() }

internal fun <T : ViewState> ViewModel.getViewStateStore(initial: () -> T): ViewStateStore<T> =
    getOrDefault(VIEW_STATE_STORE) { ViewStateStore(initial()) }

internal fun <T : ViewEvent> ViewModel.post(viewEvent: T) =
    getViewEventStore<T>() post viewEvent

internal fun <T : ViewState> ViewModel.post(viewState: T) =
    getViewStateStore { viewState } post viewState

@Suppress("UNCHECKED_CAST")
internal fun <T : ViewModel> Factory(create: () -> T) = object : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = create() as T
}

@Composable
internal inline fun <reified T : ViewModel> viewModel(noinline create: () -> T): T =
    viewModel(factory = Factory(create))
