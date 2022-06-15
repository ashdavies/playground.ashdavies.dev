package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.firstQueryParameterAsString

internal class AppCheckRequest(request: HttpRequest) {
    val appId: String by request.firstQueryParameterAsString()
    val token: String by request.firstQueryParameterAsString()
}

@Composable
internal fun rememberAppCheckRequest(request: HttpRequest = LocalHttpRequest.current): AppCheckRequest {
    return remember { AppCheckRequest(request) }
}
