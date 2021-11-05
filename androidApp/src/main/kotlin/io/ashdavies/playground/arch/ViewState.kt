package io.ashdavies.playground.arch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal interface ViewState

internal interface ViewStateStore<T : ViewState> {

    val viewState: StateFlow<T>

    fun update(transform: (T) -> T)
}

internal fun <T : ViewState> ViewStateStore(initial: T): ViewStateStore<T> =
    object : ViewStateStore<T> {

        private val _viewState = MutableStateFlow(initial)
        override val viewState: StateFlow<T> by ::_viewState

        override fun update(transform: (T) -> T) {
            _viewState.update(transform)
        }
    }

@Composable
internal fun <T : ViewState> ViewStateStore<T>.collectViewState(): State<T> =
    viewState.collectAsState()

internal infix fun <T : ViewState> ViewStateStore<T>.post(value: T) =
    update { value }
