package io.ashdavies.config

public interface RemoteConfig {
    public suspend fun <T : Any> getValue(key: String, transform: (LocalConfigValue) -> T): T
}

public suspend fun RemoteConfig.getBoolean(key: String): Boolean {
    return getValue(key) { it.asBoolean() }
}

public suspend fun RemoteConfig.getString(key: String): String {
    return getValue(key) { it.asString() }
}
