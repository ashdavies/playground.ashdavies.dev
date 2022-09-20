package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.http.runCatching
import io.ashdavies.playground.cloud.HttpException

internal fun interface AppCheckVerifier {
    suspend fun verifyToken(token: String, config: JwtOptions.() -> Unit): DecodedJWT
}

internal fun AppCheckVerifier(algorithm: Algorithm) = AppCheckVerifier { token, config ->
    runCatching({ Jwt.verify(algorithm, token, config) }) { it: JWTVerificationException ->
        throw HttpException.InvalidArgument(requireNotNull(it.message), it)
    }
}
