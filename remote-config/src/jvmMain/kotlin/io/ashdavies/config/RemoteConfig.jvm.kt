package io.ashdavies.config

public val RemoteConfig.Companion.Default: RemoteConfig
    get() = emptyRemoteConfig

private val emptyRemoteConfig = object : RemoteConfig {
    override suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T {
        return transform(EmptyLocalConfigValue)
    }
}

private object EmptyLocalConfigValue : RemoteConfigValue {
    override fun asLong() = 0L
    override fun asDouble() = 0.0
    override fun asString() = ""
    override fun asByteArray() = byteArrayOf()
    override fun asBoolean() = false
}
