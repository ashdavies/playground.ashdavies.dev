package io.ashdavies.config

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfigValue
import dev.gitlive.firebase.remoteconfig.remoteConfig

private val FirebaseRemoteConfig by lazy { Firebase.remoteConfig }

internal class LazyRemoteConfig : RemoteConfig {
    override suspend fun <T : Any> getValue(key: String, transform: (LocalConfigValue) -> T): T {
        return transform(LocalConfigValue(FirebaseRemoteConfig.getInitializedValue(key)))
    }
}

private suspend fun FirebaseRemoteConfig.getInitializedValue(key: String): FirebaseRemoteConfigValue {
    ensureInitialized()
    return getValue(key)
}
