package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import io.ashdavies.playground.cloud.HttpException.Companion.InvalidArgument
import kotlinx.serialization.Serializable

public class AppCheck internal constructor(client: AppCheckClient, algorithm: Algorithm) :
    AppCheckGenerator by AppCheckGenerator(client, algorithm),
    AppCheckVerifier by AppCheckVerifier(algorithm)

internal fun interface AppCheckGenerator {
    suspend fun createToken(request: AppCheckRequest, config: JwtOptions.() -> Unit): AppCheckToken
}

@Serializable
internal data class AppCheckToken(val token: String, val ttl: Int)

internal fun interface AppCheckVerifier {
    suspend fun verifyToken(token: String, config: JwtOptions.() -> Unit): DecodedJWT
}

private fun AppCheckGenerator(client: AppCheckClient, algorithm: Algorithm) = AppCheckGenerator { request, config ->
    runCatching<AppCheckToken, JWTVerificationException>({
        client.exchangeToken(
            Jwt.create(algorithm, config),
            request
        )
    }) {
        throw InvalidArgument(requireNotNull(it.message), it)
    }
}

private fun AppCheckVerifier(algorithm: Algorithm) = AppCheckVerifier { token, config ->
    runCatching<DecodedJWT, JWTVerificationException>({ Jwt.verify(algorithm, token, config) }) {
        throw InvalidArgument(requireNotNull(it.message), it)
    }
}

private inline fun <R, reified T : Throwable> runCatching(block: () -> R, transform: (T) -> Nothing): R {
    return runCatching(block) catch transform
}

private inline infix fun <R, reified T : Throwable> Result<R>.catch(transform: (T) -> Nothing): R {
    return fold({ it }) { if (it is T) transform(it) else throw it }
}

@Composable
public fun rememberAppCheck(algorithm: Algorithm = rememberAlgorithm()): AppCheck {
    return rememberAppCheck(rememberAppCheckClient(), algorithm)
}

@Composable
internal fun rememberAppCheck(client: AppCheckClient, algorithm: Algorithm = rememberAlgorithm()): AppCheck {
    return remember(client, algorithm) { AppCheck(client, algorithm) }
}
