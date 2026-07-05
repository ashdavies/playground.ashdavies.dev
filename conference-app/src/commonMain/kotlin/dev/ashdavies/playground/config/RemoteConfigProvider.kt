package dev.ashdavies.playground.config

import dev.ashdavies.config.firebase.rest.FirebaseRestRemoteConfig
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.BuildConfig
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.io.encoding.Base64
import kotlin.random.Random

@ContributesTo(AppScope::class)
internal interface RemoteConfigProvider {

    @Provides
    fun firebaseRestRemoteConfig(): FirebaseRestRemoteConfig {
        return FirebaseRestRemoteConfig(
            httpClient = defaultHttpClient { },
            environment = FirebaseRestRemoteConfig.Environment(
                projectId = requireNotNull(BuildConfig.GOOGLE_CLOUD_PROJECT),
                apiKey = requireNotNull(BuildConfig.API_KEY),
            ),
            request = FirebaseRestRemoteConfig.Request(
                appId = requireNotNull(BuildConfig.APP_ID),
                appInstanceId = Base64.UrlSafe
                    .encode(Random.nextBytes(17))
                    .take(22),
            ),
        )
    }
}
