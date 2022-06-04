package io.ashdavies.playground.check

internal interface AppCheckInterface {
    suspend fun createToken(appId: String, options: AppCheckTokenOptions? = null): AppCheckToken
    suspend fun verifyToken(appCheckToken: String): VerifyAppCheckTokenResponse
}
