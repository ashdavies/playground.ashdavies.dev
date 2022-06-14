package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwt.algorithms.Algorithm
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ktor.client.HttpClient
import java.security.interfaces.RSAPrivateKey

@Composable
internal fun rememberAppCheck(
    credentials: ServiceAccountCredentials = rememberGoogleCredentials(),
    client: AppCheckClient = rememberAppCheckClient(credentials),
    algorithm: Algorithm = rememberAlgorithm(credentials),
): AppCheckInterface = remember(credentials) {
    AppCheck(client, algorithm)
}

@Composable
internal fun rememberAlgorithm(
    credentials: ServiceAccountCredentials = rememberGoogleCredentials()
): Algorithm = remember(credentials) {
    Algorithm.RSA256(null, (credentials.privateKey as RSAPrivateKey))
}

@Composable
internal fun rememberAppCheckClient(
    credentials: ServiceAccountCredentials = rememberGoogleCredentials(),
    algorithm: Algorithm = rememberAlgorithm(credentials),
    httpClient: HttpClient = LocalHttpClient.current
): AppCheckClient = remember(credentials) {
    AppCheckClient(
        httpClient = httpClient,
        config = AppCheckClient.Config(
            clientEmail = credentials.clientEmail,
            projectId = credentials.projectId,
            algorithm = algorithm,
        ),
    )
}

@Composable
internal inline fun <reified T : GoogleCredentials> rememberGoogleCredentials(
    firebaseApp: FirebaseApp = LocalFirebaseApp.current
): T = remember(firebaseApp.name) {
    firebaseApp.options<FirebaseOptions, T>("getCredentials")
}

private inline operator fun <reified T : Any, reified R : Any> T.invoke(name: String): R =
    T::class.java.getDeclaredMethod(name)
        .also { it.isAccessible = true }
        .invoke(this) as R
