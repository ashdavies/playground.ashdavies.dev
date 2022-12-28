package io.ashdavies.check

import io.ashdavies.check.AppCheckToken.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.hours

private const val GOOGLE_AUTH_SCOPE = "https://www.googleapis.com/auth"
private const val GOOGLE_TOKEN_ENDPOINT = "https://accounts.google.com/o/oauth2/token"

private const val CUSTOM_EXCHANGE_URL_TEMPLATE =
    "https://firebaseappcheck.googleapis.com/v1/projects/%s/apps/%s:exchangeCustomToken"

private val FIREBASE_CLAIMS_SCOPES = listOf(
    "$GOOGLE_AUTH_SCOPE/cloud-platform",
    "$GOOGLE_AUTH_SCOPE/firebase.database",
    "$GOOGLE_AUTH_SCOPE/firebase.messaging",
    "$GOOGLE_AUTH_SCOPE/identitytoolkit",
    "$GOOGLE_AUTH_SCOPE/userinfo.email",
)

public fun interface AppCheckGenerator {
    public suspend fun createToken(appId: String): Response.Normalised
}

internal fun AppCheckGenerator(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectId: String,
) = AppCheckGenerator { appId: String ->
    val urlString = String.format(CUSTOM_EXCHANGE_URL_TEMPLATE, projectId, appId)
    val algorithm = GoogleAlgorithm(cryptoSigner)

    val assertionToken = Jwt.create(algorithm) {
        it.issuer = cryptoSigner.getAccountId()
        it.audience = GOOGLE_TOKEN_ENDPOINT
        it.scope = FIREBASE_CLAIMS_SCOPES
        it.appId = appId
    }

    val bearer = httpClient.post(GOOGLE_TOKEN_ENDPOINT) {
        contentType(ContentType.Application.FormUrlEncoded)
        parameter("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer")
        parameter("assertion", assertionToken)
    }.body<BearerResponse>()

    val customToken = Jwt.create(algorithm) {
        it.issuer = cryptoSigner.getAccountId()
        it.expiresAt = Clock.System.now() + 1.hours
        it.appId = appId
    }

    val result = httpClient.post(urlString) {
        header(HttpHeaders.Authorization, "Bearer ${bearer.accessToken}")
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

@Serializable
internal data class BearerResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
)
