package io.ashdavies.config

internal actual val firebaseRemoteConfig = object : RemoteConfig {
    override suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T {
        TODO("Not yet implemented")
    }
}
