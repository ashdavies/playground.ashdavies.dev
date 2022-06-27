package io.ashdavies.http

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProduceStateScope
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.experimental.ExperimentalTypeInference

@Composable
public fun <T : Any> produceStateInline(block: CoroutineScope.() -> T): State<Result<T>> {
    return produceState(Result.loading()) { value = runCatching(block) }
}

@Composable
public fun <T : Any> produceState(producer: suspend ProduceStateScope<Result<T>>.() -> Unit): State<Result<T>> {
    return produceState(Result.loading(), producer)
}

@Composable
public fun <T : Any> rememberState(state: StateFlow<T>): State<T> {
    val initialValue: T = remember(state) { state.value }
    return produceState(initialValue) {
        state.collect { value = it }
    }
}
