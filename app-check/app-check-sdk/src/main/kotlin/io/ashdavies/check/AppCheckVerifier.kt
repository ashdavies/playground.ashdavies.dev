package io.ashdavies.check

import com.auth0.jwt.exceptions.JWTVerificationException
import io.ashdavies.playground.cloud.HttpException

private const val APP_CHECK_ENDPOINT = "https://firebaseappcheck.googleapis.com/"

public fun interface AppCheckVerifier {
    public suspend fun verifyToken(token: String): DecodedToken
}

internal fun AppCheckVerifier(cryptoSigner: CryptoSigner, projectNumber: String): AppCheckVerifier {
    return AppCheckVerifier { token ->
        try {
            val decoded = Jwt.verify(GoogleAlgorithm(cryptoSigner), token) {
                issuer = "$APP_CHECK_ENDPOINT$projectNumber"
            }

            decoded.asDecodedToken()
        } catch (exception: JWTVerificationException) {
            throw HttpException.InvalidArgument(
                message = requireNotNull(exception.message),
                cause = exception,
            )
        }
    }
}
