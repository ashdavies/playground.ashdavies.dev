package dev.ashdavies.config.firebase.rest

import dev.ashdavies.config.RemoteConfigValue

public data class FirebaseRestRemoteConfigValue(private val value: String?) : RemoteConfigValue {
    override fun asLong(): Long = value?.toLong() ?: super.asLong()
    override fun asDouble(): Double = value?.toDouble() ?: super.asDouble()
    override fun asString(): String = value ?: super.asString()
    override fun asByteArray(): ByteArray = value?.encodeToByteArray() ?: super.asByteArray()
    override fun asBoolean(): Boolean = value?.toBoolean() ?: super.asBoolean()
}
