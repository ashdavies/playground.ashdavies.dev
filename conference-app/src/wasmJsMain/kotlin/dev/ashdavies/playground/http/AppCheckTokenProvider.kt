@file:OptIn(ExperimentalWasmJsInterop::class)

package dev.ashdavies.playground.http

import dev.ashdavies.http.common.models.AppCheckToken
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlinx.coroutines.await
import kotlin.js.Promise

@ContributesTo(AppScope::class)
internal interface AppCheckTokenProvider {

    @Provides
    suspend fun appCheckToken(): AppCheckToken {
        val result = getAppCheckToken(false).await()

        return AppCheckToken(
            token = result.token,
            ttlMillis = result.expireTimeMillis,
        )
    }
}
public external fun getAppCheckToken(forceRefresh: Boolean): Promise<AppCheckTokenResult>

public external interface AppCheckTokenResult : JsAny {
    public val expireTimeMillis: Long
    public val token: String
}
