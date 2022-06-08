package io.ashdavies.check

import com.auth0.jwt.interfaces.DecodedJWT

internal interface AppCheckInterface {
    suspend fun createToken(appId: String, options: AppCheckTokenOptions? = null): AppCheckToken
    suspend fun verifyToken(appCheckToken: String): DecodedJWT
}
