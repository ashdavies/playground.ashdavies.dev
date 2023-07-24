package io.ashdavies.notion

import kotlinx.coroutines.flow.first

internal actual suspend fun awaitAuthorizationCode(): String {
    return try {
        val callbackUri = requireNotNull(CallbackActivity.PIPE.first())
        requireNotNull(callbackUri.getQueryParameter("code"))
    } catch (exception: Exception) {
        exception.printStackTrace()
        "Failed"
    }
}

internal actual fun getRedirectUrlString(): String {
    return "https://playground.ashdavies.dev/notion"
}
