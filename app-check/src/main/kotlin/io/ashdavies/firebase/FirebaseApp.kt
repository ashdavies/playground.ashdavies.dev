package io.ashdavies.firebase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import io.ashdavies.check.AppCheck
import io.ashdavies.check.AppCheckClient
import io.ashdavies.check.AppCheckConfig
import io.ashdavies.http.LocalHttpClient
import io.ktor.client.HttpClient
import java.security.interfaces.RSAPrivateKey

@Composable
internal fun rememberAppCheck(credentials: ServiceAccountCredentials = rememberServiceAccountCredentials()): AppCheck {
    val config = rememberAppCheckConfig(credentials.clientEmail)
    val client = rememberAppCheckClient(credentials.projectId)
    return remember(credentials) { AppCheck(client, config) }
}

@Composable
private fun rememberAppCheckConfig(issuer: String, algorithm: Algorithm = rememberAlgorithm()): AppCheckConfig =
    remember(algorithm, issuer) { AppCheckConfig(algorithm, issuer) }

@Composable
private fun rememberAlgorithm(credentials: ServiceAccountCredentials = rememberServiceAccountCredentials()): Algorithm =
    remember(credentials) { Algorithm.RSA256(null, credentials.privateKey as RSAPrivateKey) }

@Composable
private fun rememberAppCheckClient(projectId: String, client: HttpClient = LocalHttpClient.current): AppCheckClient =
    remember(projectId) { AppCheckClient(client, projectId) }

@Composable
private fun rememberServiceAccountCredentials(): ServiceAccountCredentials =
    remember { GoogleCredentials.getApplicationDefault() as ServiceAccountCredentials }
