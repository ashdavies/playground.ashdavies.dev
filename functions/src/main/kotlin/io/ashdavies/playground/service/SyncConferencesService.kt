package io.ashdavies.playground.service

import io.ashdavies.playground.express.Request
import io.ashdavies.playground.express.Response

internal val SyncConferencesService: (Request, Response<String>) -> Unit =
    coroutineService { _, res -> res.send("OK") }