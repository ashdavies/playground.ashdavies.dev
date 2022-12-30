package io.ashdavies.check

import io.ashdavies.check.AppCheckToken.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

private const val CUSTOM_EXCHANGE_URL_TEMPLATE =
    "https://firebaseappcheck.googleapis.com/v1/projects/%s/apps/%s:exchangeCustomToken"

public fun interface AppCheckGenerator {
    public suspend fun createToken(appId: String): Response.Normalised
}

internal fun AppCheckGenerator(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectId: String,
) = AppCheckGenerator { appId ->
    val algorithm = GoogleAlgorithm(cryptoSigner)

    val customToken = Jwt.create(algorithm) {
        it.issuer = cryptoSigner.getAccountId()
        it.expiresAt = Clock.System.now() + 1.hours
        it.appId = appId
    }

    val bearerResponse = bearerResponse(
        accountId = cryptoSigner.getAccountId(),
        algorithm = algorithm,
        httpClient = httpClient,
        appId = appId,
    )

    val result = httpClient.post(String.format(CUSTOM_EXCHANGE_URL_TEMPLATE, projectId, appId)) {
        header(HttpHeaders.Authorization, "Bearer ${bearerResponse.accessToken}")
        header("X-Firebase-Client", "fire-admin-node/10.2.0")
        setBody(mapOf("customToken" to customToken))
    }.body<Response.Raw>()

    val ttlMillis = result.ttl
        .substring(0, result.ttl.length - 1)
        .toInt() * 1000

    Response.Normalised(
        ttlMillis = ttlMillis,
        token = result.token,
    )
}
