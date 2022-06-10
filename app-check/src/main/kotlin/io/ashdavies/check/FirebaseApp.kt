package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient
import java.security.interfaces.RSAPrivateKey

@Composable
internal fun rememberAppCheck(credentials: ServiceAccountCredentials = rememberServiceAccountCredentials()): AppCheck {
    val client: AppCheckClient = rememberAppCheckClient(credentials.projectId)
    val config: AppCheckConfig = rememberAppCheckConfig()
    return remember(credentials) { AppCheck(client, config) }
}

@Composable
internal fun rememberAppCheckConfig(algorithm: Algorithm = rememberAlgorithm()): AppCheckConfig =
    remember(algorithm) { println(algorithm); AppCheckConfig(algorithm) }

@Composable
internal fun rememberAlgorithm(credentials: ServiceAccountCredentials = rememberServiceAccountCredentials()): Algorithm =
    remember(credentials) { Algorithm.RSA256(null, credentials.privateKey as RSAPrivateKey) }

@Composable
internal fun rememberAppCheckClient(projectNumber: String, client: HttpClient = LocalHttpClient.current): AppCheckClient =
    remember(projectNumber) { AppCheckClient(client, projectNumber) }

@Composable
internal fun rememberServiceAccountCredentials(): ServiceAccountCredentials =
    remember { GoogleCredentials.getApplicationDefault() as ServiceAccountCredentials }
