package io.ashdavies.check

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.playground.cloud.HttpException.Companion.InvalidArgument

private const val APP_CHECK_ISSUER = "https://firebaseappcheck.googleapis.com/"

internal class AppCheck(private val client: AppCheckClient, private val config: AppCheckConfig) : AppCheckInterface {

    override suspend fun createToken(appId: String, options: AppCheckTokenOptions?): AppCheckToken = try {
        val token = JWT.create()
            .withIssuer(config.issuer)
            .sign(config.algorithm)

        client.exchangeToken(token, appId)
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

internal data class AppCheckConfig(val algorithm: Algorithm, val issuer: String)
