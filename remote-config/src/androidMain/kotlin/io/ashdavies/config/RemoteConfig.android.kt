package io.ashdavies.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

private const val ONE_MINUTE_IN_SECONDS = 3600L

private val firebaseRemoteConfig by lazy {
    Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = ONE_MINUTE_IN_SECONDS
        }

        setConfigSettingsAsync(configSettings)
        fetchAndActivate()
    }
}

public actual fun RemoteConfig(): RemoteConfig = object : RemoteConfig {
    override suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T {
        return transform(remoteConfigValue(firebaseRemoteConfig.getValue(key)))
    }
}

private fun remoteConfigValue(value: FirebaseRemoteConfigValue) = object : RemoteConfigValue {
    override fun asLong(): Long = value.asLong()
    override fun asDouble(): Double = value.asDouble()
    override fun asString(): String = value.asString()
    override fun asByteArray(): ByteArray = value.asByteArray()
    override fun asBoolean(): Boolean = value.asBoolean()
}
