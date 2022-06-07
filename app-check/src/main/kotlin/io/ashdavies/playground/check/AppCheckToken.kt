package io.ashdavies.playground.check

import kotlinx.serialization.Serializable

@Serializable
internal data class AppCheckToken(val token: String, val ttlMillis: Int) {
    enum class Type {
        Custom,
        Debug;
    }
}

@Serializable
internal data class AppCheckTokenOptions(val ttlMillis: Int?)
