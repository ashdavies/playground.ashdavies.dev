package dev.ashdavies.sql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.CoroutineContext

public typealias Suspended<T> = suspend () -> T

public suspend operator fun <T, R> Suspended<T>.invoke(transform: (T) -> R): R {
    return transform(invoke())
}

public fun <T, R> Suspended<T>.map(
    context: CoroutineContext = Dispatchers.Default,
    transform: (T) -> R,
): Suspended<R> = Suspended(context) {
    transform(invoke())
}

public fun <T, R> Suspended<T>.mapAsFlow(
    context: CoroutineContext = Dispatchers.Default,
    transform: (T) -> R,
): Flow<R> = Suspended(context) {
    transform(invoke())
}.asFlow()

@OptIn(ExperimentalAtomicApi::class)
public fun <T> Suspended(
    context: CoroutineContext = Dispatchers.Default,
    initializer: Suspended<T>,
): Suspended<T> = object : Suspended<T> {

    private val mutex = Mutex()
    private var value: T? = null

    override suspend operator fun invoke(): T = value ?: mutex.withLock {
        value ?: withContext(context) { initializer().also { value = it } }
    }
}
