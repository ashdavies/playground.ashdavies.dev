package io.ashdavies.playground.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val MainDispatcher: CoroutineDispatcher
    get() = Dispatchers.Main.immediate
