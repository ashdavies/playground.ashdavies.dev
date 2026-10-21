package dev.ashdavies.cloud

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import dev.ashdavies.http.common.models.AppCheckToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.time.Instant

private const val APP_CHECK_AUDIENCE = "https://firebaseappcheck.googleapis.com/google.firebase.appcheck.v1.TokenExchangeService"
private const val CLOUD_PLATFORM_SCOPE = "https://www.googleapis.com/auth/cloud-platform"

internal suspend fun HttpClient.createAppCheckToken(): AppCheckToken {
    val credentials = GoogleCredentials.getApplicationDefault().createScoped(CLOUD_PLATFORM_SCOPE)
    val signer = requireNotNull(credentials as? ServiceAccountSigner) {
        "Credentials must be of type ServiceAccountSigner to sign as a service account (found: ${credentials.javaClass})"
    }

    val projectNumber = requireNotNull(BuildConfig.PROJECT_NUMBER) { "PROJECT_NUMBER was null" }
    val appId = requireNotNull(JvmIntegrationTestBuildConfig.APP_ID) { "APP_ID was null" }
    val now = Instant.now()

    val customToken = JWT.create()
        .withIssuer(signer.account)
        .withSubject(signer.account)
        .withAudience(APP_CHECK_AUDIENCE)
        .withClaim("app_id", appId)
        .withIssuedAt(now)
        .withExpiresAt(now.plusSeconds(300))
        .sign(ServiceAccountAlgorithm(signer))

    return post("https://firebaseappcheck.googleapis.com/v1/projects/$projectNumber/apps/$appId:exchangeCustomToken") {
        bearerAuth(credentials.refreshAccessToken().tokenValue)
        setBody(mapOf("customToken" to customToken))
        contentType(ContentType.Application.Json)
    }.body()
}

private class ServiceAccountAlgorithm(private val signer: ServiceAccountSigner) : Algorithm("RS256", "SHA256withRSA") {
    override fun sign(contentBytes: ByteArray): ByteArray = signer.sign(contentBytes)
    override fun verify(jwt: DecodedJWT) = throw UnsupportedOperationException()
}
