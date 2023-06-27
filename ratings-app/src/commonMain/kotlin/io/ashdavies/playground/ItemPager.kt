package io.ashdavies.playground

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.isActive

internal fun interface ItemPager<T : Any> {
    suspend fun next(count: Int): List<T>
}

@OptIn(ExperimentalCoroutinesApi::class)
internal fun <T : Any> ItemPager(generator: suspend CoroutineScope.() -> List<T>): ItemPager<T> {
    val backgroundScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val producer = backgroundScope.produce { while (isActive) sendAll(generator()) }
    return ItemPager { count -> List(count) { producer.receive() } }
}

internal suspend fun <T : Any> ItemPager<T>.next(): T {
    return next(1).first()
}

private suspend fun <T> ProducerScope<T>.sendAll(items: List<T>) {
    items.forEach { send(it) }
}
