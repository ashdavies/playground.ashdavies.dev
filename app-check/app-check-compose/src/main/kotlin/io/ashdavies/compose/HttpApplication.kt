package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpFunction
import com.google.firebase.FirebaseApp
import io.ashdavies.check.AuthorisedHttpClient
import io.ashdavies.check.CryptoSigner
import io.ashdavies.check.GoogleAlgorithm
import io.ashdavies.check.HttpClientConfig
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpScope
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.getValue
import io.ktor.client.HttpClient

public fun AuthorisedHttpApplication(content: @Composable HttpScope.() -> Unit): HttpFunction = HttpApplication {
    CompositionLocalProvider(LocalHttpClient provides rememberAuthorisedHttpClient()) {
        content()
    }
}

@Composable
private fun rememberAuthorisedHttpClient(client: HttpClient = LocalHttpClient.current): HttpClient {
    val cryptoSigner = rememberCryptoSigner()
    val appId by LocalHttpRequest.current

    val algorithm = remember(cryptoSigner) {
        GoogleAlgorithm(cryptoSigner)
    }

    return remember(cryptoSigner, client, algorithm) {
        val config = HttpClientConfig(
            accountId = cryptoSigner.getAccountId(),
            algorithm = algorithm,
            appId = appId,
        )

        AuthorisedHttpClient(client, config)
    }
}

@Composable
private fun rememberCryptoSigner(
    firebaseApp: FirebaseApp = LocalFirebaseApp.current,
    httpClient: HttpClient = LocalHttpClient.current
): CryptoSigner = remember(firebaseApp, httpClient) {
    CryptoSigner(firebaseApp, httpClient)
}
