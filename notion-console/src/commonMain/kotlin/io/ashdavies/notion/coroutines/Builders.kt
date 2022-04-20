package io.ashdavies.notion.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

public fun <T> runBlocking(block: suspend CoroutineScope.() -> T): T = runBlocking(EmptyCoroutineContext, block)

public expect fun <T> runBlocking(context: CoroutineContext, block: suspend CoroutineScope.() -> T): T
