package io.ashdavies.playground

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlin.coroutines.CoroutineContext
import kotlin.js.Promise

internal abstract class CoroutineTest {

    private val coroutineScope: CoroutineScope = MainScope()
    private val coroutineContext: CoroutineContext
        get() = coroutineScope.coroutineContext


    protected fun runBlockingTest(
        block: suspend CoroutineScope.() -> Unit
    ): Promise<Unit> = coroutineScope.promise { block() }
}