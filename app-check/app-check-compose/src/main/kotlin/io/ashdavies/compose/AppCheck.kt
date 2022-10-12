package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import io.ashdavies.check.AppCheck
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient

@Composable
public fun rememberAppCheck(
    client: HttpClient = LocalHttpClient.current,
    algorithm: Algorithm = rememberAlgorithm(),
): AppCheck = remember(client, algorithm) {
    AppCheck(client, algorithm)
}
