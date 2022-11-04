package io.ashdavies.playground.kotlin

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public fun <T : Any, R: Any> Query<T>.mapToOneOrNull(transform: (T?) -> R): Flow<R> = asFlow()
    .mapToOneOrNull()
    .map(transform)
