package io.ashdavies.check

import kotlinx.serialization.Serializable

@Serializable
internal data class AppCheckToken(val token: String, val ttlMillis: Int)
