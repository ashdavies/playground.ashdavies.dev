package dev.ashdavies.playground.config

import dev.ashdavies.config.firebase.rest.FirebaseRestRemoteConfig
import dev.ashdavies.playground.BuildConfig
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import kotlin.io.encoding.Base64
import kotlin.random.Random

@ContributesTo(AppScope::class)
internal interface RemoteConfigProvider {

    @Provides
    fun firebaseRestRemoteConfig(httpClient: HttpClient): FirebaseRestRemoteConfig {
        return FirebaseRestRemoteConfig(
            httpClient = httpClient,
            environment = FirebaseRestRemoteConfig.Environment(
                projectId = requireNotNull(BuildConfig.GOOGLE_PROJECT_ID),
                apiKey = requireNotNull(BuildConfig.BROWSER_API_KEY),
            ),
            request = FirebaseRestRemoteConfig.Request(
                appId = requireNotNull(BuildConfig.ANDROID_APP_ID),
                appInstanceId = Base64.UrlSafe
                    .encode(Random.nextBytes(17))
                    .take(22),
            ),
        )
    }
}
