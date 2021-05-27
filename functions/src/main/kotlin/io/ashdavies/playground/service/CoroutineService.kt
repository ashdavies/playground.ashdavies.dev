package io.ashdavies.playground.service

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal fun <T> coroutineService(
    block: suspend CoroutineScope.(req: Request, res: Response<T>) -> Unit
): (Request, Response<T>) -> Unit = { req, res ->
    GlobalScope.launch {
        block(req, res)
    }
}
