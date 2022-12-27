package io.ashdavies.check

import com.google.cloud.functions.HttpMessage
import com.google.cloud.functions.HttpRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

private const val KEY_ASSERTION = "assertion"
private const val KEY_GRANT_TYPE = "grant_type"

private const val X_FIREBASE_APP_CHECK = "X-Firebase-AppCheck"

private val httpRequestCache =
    mutableMapOf<HttpRequest, Any>()

public val HttpMessage.appCheckToken: String?
    get() = headers[X_FIREBASE_APP_CHECK]?.firstOrNull()

internal fun HttpRequestBuilder.assertion(value: Any?) {
    parameter(KEY_ASSERTION, value)
}

internal fun HttpRequestBuilder.grantType(type: GrantType) {
    parameter(KEY_GRANT_TYPE, type.value)
}

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T : Any> HttpRequest.cached(): T = synchronized(this) {
    httpRequestCache.getOrPut(this) { Json.decodeFromStream<T>(inputStream) } as T
}

internal sealed interface GrantType {
    val value: Any?
}

internal object JwtBearer : GrantType {
    override val value: String = "urn:ietf:params:oauth:grant-type:jwt-bearer"
}
