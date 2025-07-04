package io.ashdavies.sql

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

public typealias Suspended<T> = suspend () -> T

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

public fun <T> Suspended(
    context: CoroutineContext = Dispatchers.Default,
    initializer: suspend () -> T,
): Suspended<T> = object : Suspended<T> {
    private val deferred = CompletableDeferred<T>()
    override suspend fun invoke(): T {
        if (!deferred.isCompleted) {
            deferred.complete(withContext(context) { initializer() })
        }
        return deferred.await()
    }
}
