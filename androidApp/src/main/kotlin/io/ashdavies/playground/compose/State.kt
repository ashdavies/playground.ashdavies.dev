package io.ashdavies.playground.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import io.ashdavies.playground.kotlin.loading

@Composable
fun <T : Any> produceState(block: suspend () -> T): State<Result<T>> = produceState(Result.loading()) {
    runCatching { block() }
}
