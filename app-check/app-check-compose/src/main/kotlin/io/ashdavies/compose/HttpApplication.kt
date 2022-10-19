package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.firebase.FirebaseApp
import io.ashdavies.check.AppCheckRequest
import io.ashdavies.check.AuthorisedHttpClient
import io.ashdavies.check.CryptoSigner
import io.ashdavies.check.GoogleAlgorithm
import io.ashdavies.check.HttpClientConfig
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpScope
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ktor.client.HttpClient
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

public fun AuthorisedHttpApplication(content: @Composable HttpScope.() -> Unit): HttpFunction = HttpApplication {
    CompositionLocalProvider(LocalHttpClient provides rememberAuthorisedHttpClient()) {
        content()
    }
}

@Composable
private fun rememberAuthorisedHttpClient(client: HttpClient = LocalHttpClient.current): HttpClient {
    val appCheckRequest = rememberAppCheckRequest()
    val cryptoSigner = rememberCryptoSigner()

    val algorithm = remember(cryptoSigner) {
        GoogleAlgorithm(cryptoSigner)
    }

    return remember(cryptoSigner, client, algorithm) {
        val config = HttpClientConfig(
            accountId = cryptoSigner.getAccountId(),
            appId = appCheckRequest.appId,
            algorithm = algorithm,
        )

        AuthorisedHttpClient(client, config)
    }
}

@Composable
@OptIn(ExperimentalSerializationApi::class)
private fun rememberAppCheckRequest(
    httpRequest: HttpRequest = LocalHttpRequest.current
): AppCheckRequest = remember(httpRequest) {
    Json.decodeFromStream(httpRequest.inputStream)
}

@Composable
private fun rememberCryptoSigner(
    firebaseApp: FirebaseApp = LocalFirebaseApp.current,
    httpClient: HttpClient = LocalHttpClient.current
): CryptoSigner = remember(firebaseApp, httpClient) {
    CryptoSigner(firebaseApp, httpClient)
}
