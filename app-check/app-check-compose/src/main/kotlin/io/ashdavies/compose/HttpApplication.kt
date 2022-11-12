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
import io.ashdavies.playground.cloud.HttpConfig
import io.ashdavies.playground.cloud.HttpScope
import io.ashdavies.playground.cloud.LocalFirebaseAdminApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ktor.client.HttpClient

public fun AuthorisedHttpApplication(content: @Composable HttpScope.() -> Unit): HttpFunction {
    return AuthorisedHttpApplication(HttpConfig.Get, content)
}

public fun AuthorisedHttpApplication(config: HttpConfig, content: @Composable HttpScope.() -> Unit): HttpFunction {
    return HttpApplication(config) {
        CompositionLocalProvider(LocalHttpClient provides rememberAuthorisedHttpClient()) {
            content()
        }
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
private fun rememberAppCheckRequest(
    httpRequest: HttpRequest = LocalHttpRequest.current
): AppCheckRequest = remember(httpRequest) {
    AppCheckRequest(httpRequest)
}

@Composable
private fun rememberCryptoSigner(
    firebaseApp: FirebaseApp = LocalFirebaseAdminApp.current,
    httpClient: HttpClient = LocalHttpClient.current
): CryptoSigner = remember(firebaseApp, httpClient) {
    CryptoSigner(firebaseApp, httpClient)
}
