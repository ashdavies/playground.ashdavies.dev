package io.ashdavies.notion

import kotlinx.coroutines.flow.first

internal actual suspend fun awaitAuthorizationCode(): String {
    val callbackUri = requireNotNull(CallbackActivity.PIPE.first())
    return requireNotNull(callbackUri.getQueryParameter("code"))
}
