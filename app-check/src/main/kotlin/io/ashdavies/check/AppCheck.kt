package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.check.AppCheckGenerator.Token
import io.ashdavies.playground.cloud.HttpException.Companion.InvalidArgument
import io.ashdavies.playground.compose.Remember
import kotlinx.serialization.Serializable

public class AppCheck @Remember internal constructor(client: AppCheckClient, algorithm: Algorithm) :
    AppCheckGenerator by AppCheckGenerator(client, algorithm),
    AppCheckVerifier by AppCheckVerifier(algorithm)

internal fun interface AppCheckGenerator {

    suspend fun createToken(request: AppCheckRequest, config: JwtOptions.() -> Unit): Token

    @Serializable
    data class Token(val token: String, val ttl: Int)
}

internal fun interface AppCheckVerifier {
    suspend fun verifyToken(token: String, config: JwtOptions.() -> Unit): DecodedJWT
}

private fun AppCheckGenerator(client: AppCheckClient, algorithm: Algorithm) = AppCheckGenerator { request, config ->
    runCatching<Token, JWTVerificationException>({ client.exchangeToken(Jwt.create(algorithm, config), request) }) {
        throw InvalidArgument(requireNotNull(it.message), it)
    }
}

private fun AppCheckVerifier(algorithm: Algorithm) = AppCheckVerifier { token, config ->
    runCatching<DecodedJWT, JWTVerificationException>({ Jwt.verify(algorithm, token, config) }) {
        throw InvalidArgument(requireNotNull(it.message), it)
    }
}
