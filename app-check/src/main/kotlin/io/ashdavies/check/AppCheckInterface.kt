package io.ashdavies.check

import com.auth0.jwt.interfaces.DecodedJWT

internal interface AppCheckInterface : AppCheckGenerator, AppCheckVerifier

internal fun interface AppCheckGenerator {
    suspend fun createToken(request: AppCheckRequest, config: JwtOptions.() -> Unit): AppCheckToken
}

internal fun interface AppCheckVerifier {
    suspend fun verifyToken(token: String): DecodedJWT
}
