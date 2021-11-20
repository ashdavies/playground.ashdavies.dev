package io.ashdavies.notion

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

actual fun <T> runBlocking(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T,
): T = runBlocking(context, block)
