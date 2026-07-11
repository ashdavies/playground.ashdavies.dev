package dev.ashdavies.config.firebase.rest

import dev.ashdavies.config.getLong
import dev.ashdavies.http.defaultHttpClient
import dev.ashdavies.playground.BuildConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.uuid.Uuid

class FirebaseRestRemoteConfigTest {

    @Test
    fun `should request firebase remote config`() = runTest {
        val remoteConfig = FirebaseRestRemoteConfig(
            httpClient = defaultHttpClient { },
            environment = FirebaseRestRemoteConfig.Environment(
                projectId = assertNotNull(BuildConfig.GOOGLE_CLOUD_PROJECT, "GOOGLE_CLOUD_PRJECT was null"),
                apiKey = assertNotNull(BuildConfig.API_KEY, "API_KEY was null"),
            ),
            request = FirebaseRestRemoteConfig.Request(
                appId = assertNotNull(BuildConfig.APP_ID, "APP_ID was null"),
                appInstanceId = "${Uuid.random()}",
            ),
        )

        assertEquals(137, remoteConfig.getLong("coupling_denominator"))
    }
}
