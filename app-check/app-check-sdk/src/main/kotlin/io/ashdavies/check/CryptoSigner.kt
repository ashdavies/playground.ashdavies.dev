package io.ashdavies.check

import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.serialization.Serializable
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
    return when (val credentials: GoogleCredentials = GoogleCredentials.getApplicationDefault()) {
        is ServiceAccountSigner -> CryptoSigner(credentials.account, credentials::sign)
        else -> IamSigner(client, getServiceAccountId(app), getToken(credentials))
    }
}

private fun IamSigner(client: HttpClient, accountId: String, token: String) = CryptoSigner(accountId) { src ->
    val urlString = "https://iamcredentials.googleapis.com/v1/projects/-/serviceAccounts/$accountId:signBlob"
    val response = client.post<IamSignedResponse>(urlString, mapOf("payload" to src.encodeToString())) {
        header(HttpHeaders.Authorization, "Bearer $token")
    }

    response
        .signedBlob
        .decodeFromString()
}

private fun getToken(credentials: GoogleCredentials): String = credentials
    .apply { refreshIfExpired() }
    .accessToken
    .tokenValue

@Serializable
internal data class IamSignedResponse(val keyId: String, val signedBlob: String)

private fun ByteArray.encodeToString(encoder: Base64.Encoder = Base64.getEncoder()): String =
    encoder.encodeToString(this)

private fun String.decodeFromString(decoder: Base64.Decoder = Base64.getDecoder()): ByteArray =
    decoder.decode(this)
