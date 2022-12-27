package io.ashdavies.check

import io.ashdavies.check.AppCheckToken.Request
import io.ashdavies.check.AppCheckToken.Response
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

private const val CUSTOM_EXCHANGE_URL_TEMPLATE =
    "https://firebaseappcheck.googleapis.com/v1/projects/%s/apps/%s:exchangeCustomToken"

internal fun interface AppCheckGenerator {
    suspend fun createToken(): Response.Normalised
}

internal fun AppCheckGenerator(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectId: String,
    appId: String,
) = AppCheckGenerator {
    val urlString = String.format(CUSTOM_EXCHANGE_URL_TEMPLATE, projectId, appId)
    val algorithm = GoogleAlgorithm(cryptoSigner)

    val customToken = Jwt.create(algorithm) {
        it.issuer = cryptoSigner.getAccountId()
        it.expiresAt = Clock.System.now() + 1.hours
        it.appId = appId
    }

    val request = Request(
        customToken = customToken,
        projectId = projectId,
        appId = appId,
    )

    val result: Response.Raw = httpClient.post(
        body = mapOf("customToken" to request.customToken),
        urlString = urlString,
    )

    val ttlMillis = result.ttl
        .substring(0, result.ttl.length - 1)
        .toInt() * 1000

    Response.Normalised(
        ttlMillis = ttlMillis,
        token = result.token,
    )
}
