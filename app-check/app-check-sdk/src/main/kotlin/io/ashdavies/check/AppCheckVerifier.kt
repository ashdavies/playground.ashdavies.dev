package io.ashdavies.check

import com.auth0.jwt.exceptions.JWTVerificationException
import io.ashdavies.playground.cloud.HttpException

private const val APP_CHECK_ENDPOINT = "https://firebaseappcheck.googleapis.com/"

public interface AppCheckVerifier {
    public suspend fun <T : Any> verifyToken(
        token: String,
        mapper: (
            audience: List<String>,
            expiresAt: Long,
            issuedAt: Long,
            subject: String,
            issuer: String,
            appId: String,
        ) -> T,
    ): T
}

internal fun AppCheckVerifier(
    cryptoSigner: CryptoSigner,
    projectNumber: String,
) = object : AppCheckVerifier {
    override suspend fun <T : Any> verifyToken(
        token: String,
        mapper: (
            audience: List<String>,
            expiresAt: Long,
            issuedAt: Long,
            subject: String,
            issuer: String,
            appId: String,
        ) -> T,
    ): T = try {
        val jwt = Jwt.verify(GoogleAlgorithm(cryptoSigner), token) {
            issuer = "$APP_CHECK_ENDPOINT$projectNumber"
        }

        mapper(
            /* audience */ jwt.audience,
            /* expiresAt */ jwt.expiresAtAsInstant.epochSecond,
            /* issuedAt */ jwt.issuedAtAsInstant.epochSecond,
            /* subject */ jwt.subject,
            /* issuer */ jwt.issuer,
            /* appId */ jwt.subject,
        )
    } catch (exception: JWTVerificationException) {
        throw HttpException.InvalidArgument(
            message = requireNotNull(exception.message),
            cause = exception,
        )
    }
}
