package io.ashdavies.playground.arch

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.setOrDefault
import androidx.lifecycle.viewmodel.compose.viewModel

private fun <T : ViewState> defaultViewStateStore(): () -> T =
    error("ViewStateStore not initialised")

internal inline fun <reified T : ViewEvent> ViewModel.getViewEventStore(): ViewEventStore<T> =
    getViewEventStore(T::class.java.simpleName)

@PublishedApi
internal fun <T : ViewEvent> ViewModel.getViewEventStore(key: String): ViewEventStore<T> =
    setOrDefault(key) { ViewEventStore() }

internal inline fun <reified T : ViewState> ViewModel.getViewStateStore(
    noinline initial: () -> T = defaultViewStateStore(),
): ViewStateStore<T> = getViewStateStore(T::class.java.simpleName, initial)

@PublishedApi
internal fun <T : ViewState> ViewModel.getViewStateStore(
    key: String,
    initial: () -> T,
): ViewStateStore<T> = setOrDefault(key) { ViewStateStore(initial()) }

internal inline infix fun <reified T : ViewEvent> ViewModel.post(viewEvent: T) =
    getViewEventStore<T>() post viewEvent

internal inline infix fun <reified T : ViewState> ViewModel.post(viewState: T) =
    getViewStateStore<T>() post viewState

internal inline fun <reified T : ViewState> ViewModel.update(noinline transform: (T) -> T) =
    getViewStateStore<T>().update(transform)

@Suppress("UNCHECKED_CAST")
internal fun <T : ViewModel> Factory(create: () -> T) = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
}

@Composable
internal inline fun <reified T : ViewModel> viewModel(noinline create: () -> T): T =
    viewModel(factory = Factory(create))
