package dev.ashdavies.playground.metro

import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.SuspendLazy
import dev.zacsweers.metro.suspendLazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalMetroCoroutinesApi
public inline fun <T, R> SuspendLazy<T>.map(
    crossinline transform: suspend (T) -> R,
): SuspendLazy<R> = suspendLazy { transform(await()) }

@ExperimentalMetroCoroutinesApi
public inline fun <T, R> SuspendLazy<T>.mapAsFlow(
    crossinline transform: suspend (T) -> R,
): Flow<R> = flow { emit(transform(await())) }
