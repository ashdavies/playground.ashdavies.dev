/** Copyright © 2020 Robert Bosch GmbH. All rights reserved. */
package io.ashdavies.playground.service

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal fun <T> coroutineService(
    block: suspend CoroutineScope.(request: Request, response: Response<T>) -> Unit
): (Request, Response<T>) -> Unit = { req, res ->
    GlobalScope.launch {
        block(req, res)
    }
}
