package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.firstQueryParameterAsString

internal interface AppCheckRequest {
    val appId: String
    val token: String
}

private fun AppCheckRequest(request: HttpRequest): AppCheckRequest = object : AppCheckRequest {
    override val appId: String by request.firstQueryParameterAsString()
    override val token: String by request.firstQueryParameterAsString()
}

@Composable
internal fun rememberAppCheckRequest(request: HttpRequest = LocalHttpRequest.current): AppCheckRequest {
    return remember(request) { println(request.uri); AppCheckRequest(request) }
}
