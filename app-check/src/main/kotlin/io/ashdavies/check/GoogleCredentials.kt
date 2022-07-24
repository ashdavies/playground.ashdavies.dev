package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.UserCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ashdavies.check.AppCheckConstants.IAM_V1_ENDPOINT
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.compose.Provides
import io.ktor.client.HttpClient
import kotlinx.coroutines.runBlocking

private const val GET_CREDENTIALS_METHOD = "getCredentials"

@Provides
@Composable
private fun rememberGoogleCredentials(app: FirebaseApp = LocalFirebaseApp.current): GoogleCredentials = remember(app) {
    FirebaseOptions::class.java
        .getDeclaredMethod(GET_CREDENTIALS_METHOD)
        .also { it.isAccessible = true }
        .invoke(app.options) as GoogleCredentials
}

@Composable
internal fun rememberAccountSigner(app: FirebaseApp = LocalFirebaseApp.current): ServiceAccountSigner {
    return when (val credentials: GoogleCredentials = rememberGoogleCredentials(app)) {
        is UserCredentials -> rememberIamSigner(app)
        is ServiceAccountSigner -> credentials
        else -> throw IllegalStateException()
    }
}

private class IamSigner(private val client: HttpClient, private val serviceAccountId: String) : ServiceAccountSigner {

    override fun sign(toSign: ByteArray): ByteArray = runBlocking { signBlob(toSign) }
    override fun getAccount(): String = serviceAccountId

    private suspend fun signBlob(toSign: ByteArray): ByteArray {
        val urlString = "$IAM_V1_ENDPOINT/-/serviceAccounts/${serviceAccountId}:signBlob"
        val body = mapOf("payload" to toSign)

        return client
            .post<Map<String, String>>(urlString, body)
            .getValue("signedBlob")
            .toByteArray()
    }
}

/**
 * TODO Debugging is possible here, discover evident credentials for FirebaseApp when debugging function
 */
@Composable
private fun rememberIamSigner(
    app: FirebaseApp = LocalFirebaseApp.current,
    client: HttpClient = LocalHttpClient.current
): ServiceAccountSigner = remember {
    if (app.options.serviceAccountId == null) {
        println("WARNING serviceAccountId is null")
    }
    IamSigner(
        serviceAccountId = app.options.serviceAccountId,
        client = client,
    )
}
