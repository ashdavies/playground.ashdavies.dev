package io.ashdavies.check

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.firstQueryParameterAsString

internal class AppCheckRequest(request: HttpRequest) {
    val appId: String by request.firstQueryParameterAsString()
    val token: String by request.firstQueryParameterAsString()
}
