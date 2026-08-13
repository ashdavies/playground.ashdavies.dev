package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.AppCheckToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
internal interface AppCheckTokenProvider {

    @Provides
    suspend fun appCheckToken(): AppCheckToken {
        TODO("REST implementation")
    }
}
