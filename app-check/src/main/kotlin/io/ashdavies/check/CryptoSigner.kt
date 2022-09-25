package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import com.google.common.annotations.VisibleForTesting
import com.google.firebase.FirebaseApp
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.compose.Provides
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import java.util.Base64

internal interface CryptoSigner {
    suspend fun sign(value: ByteArray): ByteArray
    fun getAccountId(): String
}

internal fun CryptoSigner(accountId: String, sign: suspend (value: ByteArray) -> ByteArray) = object : CryptoSigner {
    override suspend fun sign(value: ByteArray): ByteArray = sign(value)
    override fun getAccountId(): String = accountId
}

private fun CryptoSigner(app: FirebaseApp, client: HttpClient): CryptoSigner {
    return when (val credentials: GoogleCredentials = app.credentials) {
        is ServiceAccountSigner -> CryptoSigner(credentials.account, credentials::sign)
        else -> IamSigner(client, getServiceAccountId(app), getToken(credentials))
    }
}

private fun IamSigner(client: HttpClient, accountId: String, token: String) = CryptoSigner(accountId) {
    val urlString = "https://iamcredentials.googleapis.com/v1/projects/-/serviceAccounts/$accountId:signBlob"
    val payload = Base64.getEncoder().encodeToString(it)

    client.post(urlString, mapOf("payload" to payload)) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }
}

private fun getToken(credentials: GoogleCredentials): String = credentials
    .apply { refreshIfExpired() }
    .accessToken
    .tokenValue

@Provides
@Composable
internal fun rememberCryptoSigner(
    app: FirebaseApp = LocalFirebaseApp.current,
    client: HttpClient = LocalHttpClient.current,
): CryptoSigner = remember(app, client) {
    CryptoSigner(app, client)
}
