package io.ashdavies.check

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.playground.cloud.HttpException.Companion.InvalidArgument

internal const val APP_CHECK_AUDIENCE =
    "https://firebaseappcheck.googleapis.com/google.firebase.appcheck.v1.TokenExchangeService"

internal const val APP_CHECK_ISSUER =
    "https://firebaseappcheck.googleapis.com/"

internal class AppCheck(private val client: AppCheckClient, private val config: AppCheckConfig) : AppCheckInterface {

    override suspend fun createToken(appId: String, options: AppCheckTokenOptions): AppCheckToken = try {
        client.exchangeToken(createJwtToken(config.algorithm, options), appId)
    } catch (exception: JWTVerificationException) {
        throw InvalidArgument(requireNotNull(exception.message), exception)
    }

    override suspend fun verifyToken(appCheckToken: String): DecodedJWT = try {
        val verifier = JWT.require(config.algorithm)
            .withIssuer(APP_CHECK_ISSUER)
            .build()

        verifier.verify(appCheckToken)
    } catch (exception: JWTVerificationException) {
        throw InvalidArgument(requireNotNull(exception.message), exception)
    }
}

internal data class AppCheckConfig(val algorithm: Algorithm = Algorithm.none())

private fun createJwtToken(algorithm: Algorithm, options: AppCheckTokenOptions): String = JWT.create()
    .withAppId(options.appId)
    .withAudience(APP_CHECK_AUDIENCE)
    .withExpiresAt(options.expiresAt)
    .withIssuedAt(options.issuedAt)
    .withIssuer(options.issuer)
    .withSubject(options.issuer)
    .withTtl(options.ttlMillis)
    .sign(algorithm)
    .also { println("createJwtToken($algorithm, $options) = $it") }
