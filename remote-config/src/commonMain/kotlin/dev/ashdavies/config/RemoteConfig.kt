package dev.ashdavies.config

public interface RemoteConfig {
    public suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T
}

public suspend fun RemoteConfig.getBoolean(key: String): Boolean = getValue(key, RemoteConfigValue::asBoolean)

public suspend fun RemoteConfig.getLong(key: String): Long = getValue(key, RemoteConfigValue::asLong)

public suspend fun RemoteConfig.getString(key: String): String = getValue(key, RemoteConfigValue::asString)
