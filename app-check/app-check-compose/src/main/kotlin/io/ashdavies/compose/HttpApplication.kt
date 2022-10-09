package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import com.google.cloud.functions.HttpFunction
import io.ashdavies.check.AuthorisedHttpClient
import io.ashdavies.check.CryptoSigner
import io.ashdavies.check.HttpClientConfig
import io.ashdavies.check.bearerTokens
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpScope
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.getValue
import io.ktor.client.HttpClient

public fun AuthorisedHttpApplication(content: @Composable HttpScope.() -> Unit): HttpFunction = HttpApplication {
    CompositionLocalProvider(LocalHttpClient provides rememberAuthorisedHttpClient()) {
        content()
    }
}

@Composable
private fun rememberAuthorisedHttpClient(): HttpClient {
    val signer: CryptoSigner = rememberCryptoSigner()
    val client: HttpClient = LocalHttpClient.current
    val algorithm: Algorithm = rememberAlgorithm()
    val appId: String by LocalHttpRequest.current

    return remember(signer, client, algorithm) {
        val config = HttpClientConfig(
            accountId = signer.getAccountId(),
            algorithm = algorithm,
            appId = appId,
        )

        AuthorisedHttpClient(client) {
            client.bearerTokens(config)
        }
    }
}
