package io.ashdavies.playground.check

import io.ashdavies.playground.cloud.HttpException

internal class AppCheckClient {
    fun exchangeToken(customToken: String, appId: String): AppCheckToken = TODO()
}

internal fun AppCheckError(message: String) = HttpException.BadRequest(message)
