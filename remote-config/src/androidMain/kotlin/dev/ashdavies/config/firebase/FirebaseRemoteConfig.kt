package dev.ashdavies.config.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.remoteConfigSettings
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.RemoteConfigValue

private const val ONE_MINUTE_IN_SECONDS = 3600L

public class FirebaseRemoteConfig(firebaseApp: FirebaseApp) : RemoteConfig {

    private val firebaseRemoteConfig = com.google.firebase.remoteconfig.FirebaseRemoteConfig
        .getInstance(firebaseApp)
        .also { it.configure() }

    override suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T {
        return transform(FirebaseRemoteConfigValue(firebaseRemoteConfig.getValue(key)))
    }
}

private fun com.google.firebase.remoteconfig.FirebaseRemoteConfig.configure() {
    val settings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = ONE_MINUTE_IN_SECONDS
    }

    setConfigSettingsAsync(settings)
    fetchAndActivate()
}
