package io.ashdavies.playground.kotlin

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

public fun <T : Any, R : Any?> Query<T>.mapToOneOrNull(
    context: CoroutineContext = Dispatchers.Default,
    transform: suspend (value: T?) -> R,
): Flow<R> = asFlow()
    .mapToOneOrNull(context)
    .map(transform)

public fun <T : Any, R : Any> Query<T>.mapToOne(
    context: CoroutineContext = Dispatchers.Default,
    transform: suspend (value: T) -> R
): Flow<R> = asFlow()
    .mapToOne(context)
    .map(transform)

public fun <T : Any> Query<T>.mapToOne(): Flow<T> {
    return mapToOne { it }
}
