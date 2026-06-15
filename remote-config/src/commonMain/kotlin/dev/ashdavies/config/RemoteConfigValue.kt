package dev.ashdavies.config

public interface RemoteConfigValue {
    public fun asLong(): Long = 0L
    public fun asDouble(): Double = 0.0
    public fun asString(): String = ""
    public fun asByteArray(): ByteArray = byteArrayOf()
    public fun asBoolean(): Boolean = false
}
