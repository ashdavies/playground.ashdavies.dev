package io.ashdavies.http

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import kotlinx.coroutines.CoroutineScope

@Composable
public fun <T : Any> produceStateInline(block: suspend CoroutineScope.() -> T): State<Result<T>> {
    return produceState(Result.loading()) { value = runCatching { block() } }
}
