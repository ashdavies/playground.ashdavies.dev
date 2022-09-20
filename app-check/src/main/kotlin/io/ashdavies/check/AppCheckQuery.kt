package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.firstQueryParameterAsString
import io.ashdavies.playground.compose.Remember

internal class AppCheckQuery @Remember constructor(request: HttpRequest) {
    val appKey: String by request.firstQueryParameterAsString()
    val appId: String by request.firstQueryParameterAsString()
}

@Composable
internal fun rememberAppCheckRequest(request: HttpRequest = LocalHttpRequest.current): AppCheckQuery {
    return remember { AppCheckQuery(request) }
}
