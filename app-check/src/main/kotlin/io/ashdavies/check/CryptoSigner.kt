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
import java.util.Base64

internal interface CryptoSigner {
    suspend fun sign(value: ByteArray): ByteArray
    fun getAccountId(): String
}

internal fun CryptoSigner(accountId: String, sign: suspend (value: ByteArray) -> ByteArray) = object : CryptoSigner {
    override suspend fun sign(value: ByteArray): ByteArray = sign(value)
    override fun getAccountId(): String = accountId
}

@VisibleForTesting
internal fun CryptoSigner(app: FirebaseApp, client: HttpClient): CryptoSigner {
    return when (val credentials: GoogleCredentials = app.credentials) {
        is ServiceAccountSigner -> CryptoSigner(credentials.account, credentials::sign)
        else -> IamSigner(client, findExplicitServiceAccountId(app)!!)
    }
}

@Provides
@Composable
internal fun rememberCryptoSigner(app: FirebaseApp = LocalFirebaseApp.current): CryptoSigner {
    val client: HttpClient = LocalHttpClient.current
    return remember(app, client) {
        CryptoSigner(app, client)
    }
}

private fun IamSigner(client: HttpClient, serviceAccountId: String) = CryptoSigner(serviceAccountId) {
    val urlString = "https://iamcredentials.googleapis.com/v1/projects/-/serviceAccounts/$serviceAccountId:signBlob"
    val encoder = Base64.getEncoder()

    client.post(
        body = mapOf("payload" to encoder.encodeToString(it)),
        urlString = urlString,
    )
}
