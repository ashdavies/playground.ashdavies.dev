package io.ashdavies.check

import com.google.cloud.functions.HttpRequest
import kotlinx.serialization.Serializable
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Serializable
public data class AppCheckRequest(val appId: String)

public fun AppCheckRequest(request: HttpRequest): AppCheckRequest {
    val urlDecode = { it: String -> URLDecoder.decode(it, StandardCharsets.UTF_8) }
    val appCheckRequest = request.cached<AppCheckRequest>()

    return appCheckRequest.run {
        copy(appId = urlDecode(appId))
    }
}
