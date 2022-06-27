package io.ashdavies.playground

import kotlinx.serialization.Serializable

@Serializable
public data class AppCheckToken(val token: String, val expireTimeMillis: Long)
