package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ashdavies.check.AppCheckToken.Request
import io.ashdavies.check.AppCheckToken.Response
import io.ktor.client.HttpClient

private const val CUSTOM_EXCHANGE_URL_TEMPLATE =
    "https://firebaseappcheck.googleapis.com/v1/projects/%s/apps/%s:exchangeCustomToken"

internal fun interface AppCheckGenerator {
    suspend fun createToken(request: Request.Raw, config: (JwtOptions) -> Unit): Response.Normalised
}

internal fun AppCheckGenerator(client: HttpClient, algorithm: Algorithm) = AppCheckGenerator { request, config ->
    val urlString = String.format(CUSTOM_EXCHANGE_URL_TEMPLATE, request.projectId, request.appId)
    val processed = Request.Processed(Jwt.create(algorithm, config), request.projectId, request.appId)

    val result: Response.Raw = client.post(
        body = mapOf("customToken" to processed.customToken),
        urlString = urlString,
    )

    val ttlMillis: Int = result.ttl
        .substring(0, result.ttl.length - 1)
        .toInt() * 1000

    Response.Normalised(
        ttlMillis = ttlMillis,
        token = result.token,
    )
}
