package dev.ashdavies.config.firebase

import dev.ashdavies.config.RemoteConfigValue

internal class FirebaseRemoteConfigValue(
    private val value: com.google.firebase.remoteconfig.FirebaseRemoteConfigValue,
) : RemoteConfigValue {
    override fun asLong() = value.asLong()
    override fun asDouble() = value.asDouble()
    override fun asString() = value.asString()
    override fun asByteArray() = value.asByteArray()
    override fun asBoolean() = value.asBoolean()
}
