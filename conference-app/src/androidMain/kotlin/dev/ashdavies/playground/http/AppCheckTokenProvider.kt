package dev.ashdavies.playground.http

import com.google.firebase.appcheck.FirebaseAppCheck
import dev.ashdavies.analytics.RemoteAnalytics
import dev.ashdavies.http.common.models.AppCheckToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.tasks.await

@ContributesTo(AppScope::class)
internal interface AppCheckTokenProvider {

    @Provides
    suspend fun appCheckToken(analytics: RemoteAnalytics): AppCheckToken = AppCheckToken(
        token = FirebaseAppCheck.getInstance()
            .getAppCheckToken(true)
            .await()
            .token,
    )
}
