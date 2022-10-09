package io.ashdavies.check

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.getValue

internal class AppCheckQuery(request: HttpRequest) : AppCheckRequest {
    override val appId: String by request
    val appKey: String by request
}
