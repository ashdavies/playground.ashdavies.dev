package io.ashdavies.config

import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfigValue

public interface LocalConfigValue {
    public fun asLong(): Long
    public fun asDouble(): Double
    public fun asString(): String
    public fun asByteArray(): ByteArray
    public fun asBoolean(): Boolean
}

internal fun LocalConfigValue(value: FirebaseRemoteConfigValue): LocalConfigValue {
    return object : LocalConfigValue {
        override fun asLong(): Long = value.asLong()
        override fun asDouble(): Double = value.asDouble()
        override fun asString(): String = value.asString()
        override fun asByteArray(): ByteArray = value.asByteArray()
        override fun asBoolean(): Boolean = value.asBoolean()
    }
}
