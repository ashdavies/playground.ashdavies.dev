package io.ashdavies.sql

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

public fun <T : Any> Query<T>.mapToList(
    context: CoroutineContext,
): Flow<List<T>> = mapToList(context) { it }

public fun <T : Any, R : Any?> Query<T>.mapToList(
    context: CoroutineContext,
    transform: suspend (value: T) -> R,
): Flow<List<R>> = asFlow()
    .mapToList(context)
    .mapList(transform)

private fun <T : Any, R : Any?> Flow<List<T>>.mapList(
    transform: suspend (value: T) -> R,
): Flow<List<R>> = map { list ->
    list.map { transform(it) }
}

public fun <T : Any, R : Any?> Query<T>.mapToOneOrNull(
    context: CoroutineContext,
    transform: suspend (value: T?) -> R,
): Flow<R> = asFlow()
    .mapToOneOrNull(context)
    .map(transform)

public fun <T : Any, R : Any> Query<T>.mapToOne(
    context: CoroutineContext,
    transform: suspend (value: T) -> R,
): Flow<R> = asFlow()
    .mapToOne(context)
    .map(transform)

public fun <T : Any> Query<T>.mapToOne(
    context: CoroutineContext,
): Flow<T> = mapToOne(context) { it }
