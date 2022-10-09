package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ashdavies.check.AppCheckToken.Request
import io.ashdavies.check.AppCheckToken.Response
import io.ktor.client.HttpClient

private const val APP_CHECK_V1_API = "https://firebaseappcheck.googleapis.com/v1/projects"

internal fun interface AppCheckGenerator {
    suspend fun createToken(request: Request.Raw, config: (JwtOptions) -> Unit): Response.Normalised
}

internal fun AppCheckGenerator(client: HttpClient, algorithm: Algorithm) = AppCheckGenerator { request, config ->
    val urlString = "$APP_CHECK_V1_API/${request.projectId}/apps/${request.appId}:exchangeCustomToken"
    val jwtToken = Jwt.create(algorithm, config)

    println("=== TOKEN $jwtToken ===")

    val tokenRequest = Request.Processed(jwtToken, request.projectId, request.appId)

    val result: Response.Raw = client.post(
        body = mapOf("customToken" to tokenRequest.customToken),
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
