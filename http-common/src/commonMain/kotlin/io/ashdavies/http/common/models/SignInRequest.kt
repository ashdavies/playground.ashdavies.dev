package io.ashdavies.http.common.models

import kotlinx.serialization.Serializable

@Serializable
public data class SignInRequest(
    public val uid: String,
)
