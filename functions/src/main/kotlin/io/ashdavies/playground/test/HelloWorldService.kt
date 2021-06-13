package io.ashdavies.playground.test

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response
import io.ashdavies.playground.core.coroutineService

internal fun HelloWorldService(): HelloWorldService = coroutineService { _, res ->
    res.send("Hello World")
}

internal typealias HelloWorldService = (Request, Response<String>) -> Unit