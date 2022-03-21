package io.ashdavies.playground.kotlin

import com.kuuurt.paging.multiplatform.helpers.dispatcher
import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public interface CloseableFlow<T> : Flow<T> {
    fun watch(block: suspend (T) -> Unit): Closeable
}

public fun <T> Flow<T>.asCloseableFlow(): CloseableFlow<T> = CloseableFlowImpl(this)

private class CloseableFlowImpl<T>(private val source: Flow<T>) : CloseableFlow<T>, Flow<T> by source {
    override fun watch(block: suspend (T) -> Unit): Closeable {
        val coroutineScope = CoroutineScope(dispatcher())
        onEach { block(it) }.launchIn(coroutineScope)
        return Closeable { coroutineScope.cancel() }
    }
}

private fun Closeable(block: () -> Unit) = object : Closeable {
    override fun close() = block()
}
