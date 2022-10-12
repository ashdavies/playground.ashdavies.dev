package io.ashdavies.check

import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import java.util.Base64

public interface CryptoSigner {
    public suspend fun sign(value: ByteArray): ByteArray
    public fun getAccountId(): String
}

internal fun CryptoSigner(accountId: String, sign: suspend (value: ByteArray) -> ByteArray) = object : CryptoSigner {
    override suspend fun sign(value: ByteArray): ByteArray = sign(value)
    override fun getAccountId(): String = accountId
}

public fun CryptoSigner(app: FirebaseApp, client: HttpClient): CryptoSigner {
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
