package io.ashdavies.check

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.hours

private const val CUSTOM_EXCHANGE_URL_TEMPLATE =
    "https://firebaseappcheck.googleapis.com/v1/projects/%s/apps/%s:exchangeCustomToken"

public interface AppCheckGenerator {
    public suspend fun <T : Any> createToken(
        appId: String,
        mapper: (
            ttlMillis: Long,
            token: String,
        ) -> T,
    ): T
}

internal fun AppCheckGenerator(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectId: String,
) = object : AppCheckGenerator {
    override suspend fun <T : Any> createToken(
        appId: String,
        mapper: (
            ttlMillis: Long,
            token: String,
        ) -> T,
    ): T {
        val algorithm = GoogleAlgorithm(cryptoSigner)

        val customToken = Jwt.create(algorithm) {
            it.expiresAt = Clock.System.now() + 1.hours
            it.issuer = cryptoSigner.getAccount()
            it.appId = appId
        }

        val bearerResponse = bearerResponse(
            accountId = cryptoSigner.getAccount(),
            httpClient = httpClient,
            algorithm = algorithm,
            appId = appId,
        )

        val result = httpClient.post(String.format(CUSTOM_EXCHANGE_URL_TEMPLATE, projectId, appId)) {
            header(HttpHeaders.Authorization, "Bearer ${bearerResponse.accessToken}")
            header("X-Firebase-Client", "fire-admin-node/10.2.0")
            setBody(mapOf("customToken" to customToken))
        }.body<CustomTokenResponse>()

        val ttlMillis = result.ttl
            .substring(0, result.ttl.length - 1)
            .toLong() * 1000

        return mapper(
            /* ttlMillis */ ttlMillis,
            /* token */ result.token,
        )
    }
}

@Serializable
private data class CustomTokenResponse(
    val token: String,
    val ttl: String,
)
