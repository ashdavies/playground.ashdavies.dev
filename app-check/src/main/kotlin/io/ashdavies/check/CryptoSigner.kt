package io.ashdavies.check

import androidx.compose.runtime.Composable
import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.compose.Provides
import io.ktor.client.HttpClient
import java.util.Base64

internal interface CryptoSigner {
    suspend fun sign(value: ByteArray): ByteArray
    fun getAccountId(): String
}

internal fun CryptoSigner(accountId: String, sign: suspend (value: ByteArray) -> ByteArray) = object : CryptoSigner {
    override suspend fun sign(value: ByteArray): ByteArray = sign(value)
    override fun getAccountId(): String = accountId
}

@Provides
@Composable
internal fun rememberCryptoSigner(app: FirebaseApp = LocalFirebaseApp.current): CryptoSigner {
    return when (val credentials: GoogleCredentials = rememberGoogleCredentials(app)) {
        is ServiceAccountSigner -> CryptoSigner(credentials.account, credentials::sign)
        else -> rememberIamSigner()
    }
}

@Composable
private fun rememberIamSigner(
    serviceAccountId: String = getServiceAccountId(),
    encoder: Base64.Encoder = Base64.getEncoder(),
    client: HttpClient = LocalHttpClient.current,
): CryptoSigner = CryptoSigner(serviceAccountId) {
    client.post(
        urlString = "https://iamcredentials.googleapis.com/v1/projects/-/serviceAccounts/$serviceAccountId:signBlob",
        body = mapOf("payload" to encoder.encodeToString(it)),
    )
}
