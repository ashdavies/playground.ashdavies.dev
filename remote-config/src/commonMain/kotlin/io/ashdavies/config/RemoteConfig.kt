package io.ashdavies.config

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfigValue
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig

public interface RemoteConfig {

    public suspend fun <T : Any> getValue(
        key: String,
        transform: (FirebaseRemoteConfigValue) -> T,
    ): T

    public companion object Default : RemoteConfig {

        private val remoteConfig = Firebase.remoteConfig

        init {
            remoteConfig.android.addOnConfigUpdateListener(
                object : ConfigUpdateListener {
                    override fun onUpdate(configUpdate: ConfigUpdate) = Unit
                    override fun onError(error: FirebaseRemoteConfigException) = Unit
                },
            )
        }

        override suspend fun <T : Any> getValue(
            key: String,
            transform: (FirebaseRemoteConfigValue) -> T,
        ): T {
            remoteConfig.ensureInitialized()
            return transform(remoteConfig[key])
        }
    }
}

public suspend fun RemoteConfig.getBoolean(key: String): Boolean {
    return getValue(key) { it.asBoolean() }
}

public suspend fun RemoteConfig.getString(key: String): String {
    return getValue(key) { it.asString() }
}
