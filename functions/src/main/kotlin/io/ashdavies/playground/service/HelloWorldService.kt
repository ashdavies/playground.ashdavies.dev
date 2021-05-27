package io.ashdavies.playground.service

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response

internal fun HelloWorldService(): HelloWorldService = coroutineService { _, res ->
    res.send("Hello World")
}

internal typealias HelloWorldService = (Request, Response<String>) -> Unit