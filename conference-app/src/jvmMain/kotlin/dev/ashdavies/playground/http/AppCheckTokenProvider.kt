package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.AppCheckToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@ContributesTo(AppScope::class)
internal interface AppCheckTokenProvider {

    @Provides
    suspend fun appCheckToken(appCheckTokenServer: AppCheckTokenServer): AppCheckToken {
        return appCheckTokenServer.state
            .filterIsInstance<AppCheckTokenState.Done>()
            .map { it.token }
            .first()
    }
}
