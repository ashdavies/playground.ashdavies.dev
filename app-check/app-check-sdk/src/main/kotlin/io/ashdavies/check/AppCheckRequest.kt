package io.ashdavies.check

import com.google.cloud.functions.HttpRequest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

private val appCheckRequestCache = mutableMapOf<HttpRequest, AppCheckRequest>()

@Serializable
public data class AppCheckRequest(val appId: String)

@OptIn(ExperimentalSerializationApi::class)
public fun AppCheckRequest(request: HttpRequest): AppCheckRequest = synchronized(request) {
    appCheckRequestCache.getOrPut(request) { Json.decodeFromStream(request.inputStream) }
}
