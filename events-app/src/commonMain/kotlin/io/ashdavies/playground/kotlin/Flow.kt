package io.ashdavies.playground.kotlin

import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public interface CloseableFlow<T> : Flow<T> {
    public fun watch(block: suspend (T) -> Unit): Closeable
}

public fun <T> Flow<T>.asCloseableFlow(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
): CloseableFlow<T> = CloseableFlowImpl(dispatcher, this)

private class CloseableFlowImpl<T>(
    private val dispatcher: CoroutineDispatcher,
    private val source: Flow<T>,
) : CloseableFlow<T>, Flow<T> by source {
    override fun watch(block: suspend (T) -> Unit): Closeable {
        val coroutineScope = CoroutineScope(dispatcher)
        onEach { block(it) }.launchIn(coroutineScope)
        return Closeable { coroutineScope.cancel() }
    }
}
