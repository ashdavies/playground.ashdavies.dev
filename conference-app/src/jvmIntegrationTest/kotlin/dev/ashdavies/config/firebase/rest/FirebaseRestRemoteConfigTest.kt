package dev.ashdavies.config.firebase.rest

import dev.ashdavies.config.getLong
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.BuildConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

class FirebaseRestRemoteConfigTest {

    @Test
    fun `should request firebase remote config`() = runTest {
        val remoteConfig = FirebaseRestRemoteConfig(
            httpClient = defaultHttpClient { },
            environment = FirebaseRestRemoteConfig.Environment(
                projectId = requireNotNull(BuildConfig.GOOGLE_PROJECT_ID),
                apiKey = requireNotNull(BuildConfig.BROWSER_API_KEY),
            ),
            request = FirebaseRestRemoteConfig.Request(
                appId = requireNotNull(BuildConfig.BROWSER_APP_ID),
                appInstanceId = "${Uuid.random()}",
            ),
        )

        assertEquals(137, remoteConfig.getLong("coupling_denominator"))
    }
}
