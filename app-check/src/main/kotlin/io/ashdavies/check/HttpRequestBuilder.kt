package io.ashdavies.check

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter

private const val KEY_ASSERTION = "assertion"
private const val KEY_GRANT_TYPE = "grant_type"

internal fun HttpRequestBuilder.assertion(value: Any?) {
    parameter(KEY_ASSERTION, value)
}

internal fun HttpRequestBuilder.grantType(type: GrantType) {
    parameter(KEY_GRANT_TYPE, type.value)
}

internal sealed interface GrantType {
    val value: Any?
}

internal object JwtBearer : GrantType {
    override val value: String = "urn:ietf:params:oauth:grant-type:jwt-bearer"
}
