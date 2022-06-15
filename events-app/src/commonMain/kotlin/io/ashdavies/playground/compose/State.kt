package io.ashdavies.playground.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.arkivanov.essenty.lifecycle.Lifecycle
import io.ashdavies.playground.kotlin.loading
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

@Composable
fun <T : Any> produceState(block: suspend () -> T): State<Result<T>> = produceState(Result.loading()) {
    value = runCatching { block() }
}

@Composable
fun <T : Any> rememberState(state: StateFlow<T>): State<T> {
    val initialValue: T = remember(state) { state.value }
    return produceState(initialValue) {
        state.collect { value = it }
    }
}
