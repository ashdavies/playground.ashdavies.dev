package io.ashdavies.check

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.playground.cloud.HttpException

internal fun interface AppCheckVerifier {
    suspend fun verifyToken(token: String, config: JwtOptions.() -> Unit): DecodedJWT
}

internal fun AppCheckVerifier(cryptoSigner: CryptoSigner) = AppCheckVerifier { token, config ->
    try {
        Jwt.verify(GoogleAlgorithm(cryptoSigner), token, config)
    } catch (exception: JWTVerificationException) {
        throw HttpException.InvalidArgument(requireNotNull(exception.message), exception)
    }
}
