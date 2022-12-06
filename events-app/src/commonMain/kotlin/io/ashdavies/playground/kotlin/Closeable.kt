package io.ashdavies.playground.kotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public expect interface Closeable

public expect fun Closeable(close: () -> Unit): Closeable

public interface CloseableFlow<T> : Flow<T> {
    public fun watch(block: suspend (T) -> Unit): Closeable
}

public fun <T> Flow<T>.asCloseableFlow(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
): CloseableFlow<T> = object : CloseableFlow<T>, Flow<T> by this {
    override fun watch(block: suspend (T) -> Unit): Closeable {
        val coroutineScope = CoroutineScope(dispatcher)
        onEach { block(it) }.launchIn(coroutineScope)
        return Closeable { coroutineScope.cancel() }
    }
}
