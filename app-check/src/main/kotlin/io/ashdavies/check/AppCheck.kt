package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import io.ashdavies.playground.cloud.HttpException.Companion.InvalidArgument

internal fun AppCheck(client: AppCheckClient, algorithm: Algorithm): AppCheckInterface =
    object : AppCheckInterface, AppCheckGenerator by AppCheckGenerator(client, algorithm),
        AppCheckVerifier by AppCheckVerifier(algorithm) {}

private fun AppCheckGenerator(client: AppCheckClient, algorithm: Algorithm) = AppCheckGenerator { request, config ->
    runCatching { client.exchangeToken(Jwt.create(algorithm, config), request) } catch { exception: JWTVerificationException ->
        throw InvalidArgument(requireNotNull(exception.message), exception)
    }
}

private fun AppCheckVerifier(algorithm: Algorithm) = AppCheckVerifier {
    runCatching { Jwt.verify(algorithm, it) } catch { exception: JWTVerificationException ->
        throw InvalidArgument(requireNotNull(exception.message), exception)
    }
}

private inline infix fun <R, reified T : Throwable> Result<R>.catch(transform: (T) -> Nothing): R {
    return fold({ it }) { if (it is T) transform(it) else throw it }
}
