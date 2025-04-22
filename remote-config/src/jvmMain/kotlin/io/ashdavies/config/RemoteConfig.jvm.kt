package io.ashdavies.config

public actual fun RemoteConfig(): RemoteConfig = object : RemoteConfig {
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
