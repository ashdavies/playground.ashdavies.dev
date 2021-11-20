package io.ashdavies.notion

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val AUTHORIZATION_CODE = "authorization_code"

@Serializable
internal data class TokenRequest(
    @SerialName("grant_type") val grantType: String,
    @SerialName("code") val code: String,
    @SerialName("redirect_uri") val redirectUrl: String,
)

internal fun TokenRequest(code: String, redirectUrl: String) = TokenRequest(
    grantType = AUTHORIZATION_CODE,
    redirectUrl = redirectUrl,
    code = code,
)
