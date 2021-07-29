package io.ashdavies.playground.arch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal interface ViewState

internal interface ViewStateStore<T : ViewState> {

    val viewState: StateFlow<T>

    infix fun post(viewState: T)
}

private class ViewStateStoreImpl<T : ViewState>(initial: T) : ViewStateStore<T> {

    private val _viewState = MutableStateFlow(initial)
    override val viewState: StateFlow<T>
        get() = _viewState

    override fun post(viewState: T) {
        _viewState.value = viewState
    }
}

internal fun <T : ViewState> ViewStateStore(initial: T): ViewStateStore<T> =
    ViewStateStoreImpl(initial)

@Composable
internal fun <T : ViewState> ViewStateStore<T>.collectViewState(): State<T> =
    viewState.collectAsState()
