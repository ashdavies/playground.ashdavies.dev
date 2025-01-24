package io.ashdavies.config

public interface RemoteConfig {
    public suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T
    public companion object
}

public suspend fun RemoteConfig.getBoolean(key: String): Boolean {
    return getValue(key) { it.asBoolean() }
}
