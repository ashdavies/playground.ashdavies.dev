@file:JvmName("BuildersJvm")

package io.ashdavies.notion.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

public actual fun <T> runBlocking(context: CoroutineContext, block: suspend CoroutineScope.() -> T): T {
    return runBlocking(context, block)
}
