package io.ashdavies.playground.check

import kotlinx.serialization.Serializable

@Serializable
internal data class AppCheckToken(val ttlMillis: Int, val token: String)

@Serializable
internal data class AppCheckTokenOptions(val ttlMillis: Int?)

@Serializable
internal class DecodedAppCheckToken
