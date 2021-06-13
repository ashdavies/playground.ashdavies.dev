package io.ashdavies.playground.core

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.express.error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal fun <T> coroutineService(
    block: suspend CoroutineScope.(req: Request, res: Response<T>) -> Unit
): (Request, Response<T>) -> Unit = { req, res ->
    GlobalScope.launch {
        try {
            block(req, res)
        } catch (exception: Exception) {
            res.error(500, exception.message)
        }
    }
}
