package io.ashdavies.config

public interface RemoteConfigValue {
    public fun asLong(): Long
    public fun asDouble(): Double
    public fun asString(): String
    public fun asByteArray(): ByteArray
    public fun asBoolean(): Boolean
}
