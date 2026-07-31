package dev.ashdavies.config.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.remoteConfigSettings
import dev.ashdavies.config.RemoteConfig
import dev.ashdavies.config.RemoteConfigValue
import dev.zacsweers.metro.ExperimentalMetroCoroutinesApi
import dev.zacsweers.metro.suspendLazy
import kotlinx.coroutines.tasks.await
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMetroCoroutinesApi::class)
public class FirebaseRemoteConfig(firebaseApp: FirebaseApp) : RemoteConfig {

    private val firebaseRemoteConfig = suspendLazy {
        com.google.firebase.remoteconfig.FirebaseRemoteConfig
            .getInstance(firebaseApp)
            .also { it.configure() }
    }

    override suspend fun <T : Any> getValue(key: String, transform: (RemoteConfigValue) -> T): T {
        return transform(FirebaseRemoteConfigValue(firebaseRemoteConfig.await().getValue(key)))
    }
}

private suspend fun com.google.firebase.remoteconfig.FirebaseRemoteConfig.configure(): Boolean {
    val settings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 1.minutes.inWholeSeconds
    }

    setConfigSettingsAsync(settings)
    return fetchAndActivate().await()
}
