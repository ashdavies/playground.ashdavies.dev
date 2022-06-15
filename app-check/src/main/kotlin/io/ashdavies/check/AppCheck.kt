package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.check.AppCheckConstants.APP_CHECK_ENDPOINT
import io.ashdavies.playground.cloud.HttpException.Companion.InvalidArgument

internal interface AppCheck : AppCheckGenerator, AppCheckVerifier

internal fun interface AppCheckGenerator {
    suspend fun createToken(request: AppCheckRequest, config: JwtOptions.() -> Unit): AppCheckToken
}

internal fun interface AppCheckVerifier {
    suspend fun verifyToken(token: String, config: JwtOptions.() -> Unit): DecodedJWT
}

internal fun AppCheck(client: AppCheckClient, algorithm: Algorithm): AppCheck = object : AppCheck,
    AppCheckGenerator by AppCheckGenerator(client, algorithm),
    AppCheckVerifier by AppCheckVerifier(algorithm) { }

private fun AppCheckGenerator(client: AppCheckClient, algorithm: Algorithm) = AppCheckGenerator { request, config ->
    runCatching { client.exchangeToken(Jwt.create(algorithm, config), request) } catch { exception: JWTVerificationException ->
        throw InvalidArgument(requireNotNull(exception.message), exception)
    }
}

private fun AppCheckVerifier(algorithm: Algorithm) = AppCheckVerifier { token, config ->
    runCatching { Jwt.verify(algorithm, token, config) } catch { exception: JWTVerificationException ->
        throw InvalidArgument(requireNotNull(exception.message), exception)
    }
}

private inline infix fun <R, reified T : Throwable> Result<R>.catch(transform: (T) -> Nothing): R {
    return fold({ it }) { if (it is T) transform(it) else throw it }
}
