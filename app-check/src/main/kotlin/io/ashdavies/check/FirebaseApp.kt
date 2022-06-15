package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.auth0.jwk.UrlJwkProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ashdavies.check.AppCheckConstants.APP_CHECK_PUBLIC_KEY
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ktor.client.HttpClient
import java.net.URL
import java.security.interfaces.RSAPrivateKey
import com.auth0.jwt.algorithms.Algorithm as JwtAlgorithm

@Composable
internal fun rememberAppCheck(
    credentials: ServiceAccountCredentials = rememberGoogleCredentials(),
    client: AppCheckClient = rememberAppCheckClient(credentials),
    algorithm: JwtAlgorithm = rememberAlgorithm(credentials),
): AppCheck = remember(credentials) {
    AppCheck(client, algorithm)
}

@Composable
internal inline fun <reified T : GoogleCredentials> rememberGoogleCredentials(
    firebaseApp: FirebaseApp = LocalFirebaseApp.current
): T = remember(firebaseApp.name) {
    firebaseApp.options<FirebaseOptions, T>("getCredentials")
}

@Composable
internal fun rememberAppCheckClient(
    credentials: ServiceAccountCredentials = rememberGoogleCredentials(),
    algorithm: JwtAlgorithm = rememberAlgorithm(credentials),
    client: HttpClient = LocalHttpClient.current
): AppCheckClient = remember(credentials) {
    AppCheckClient(
        client, AppCheckClient.Config(
            clientEmail = credentials.clientEmail,
            projectId = credentials.projectId,
            algorithm = algorithm,
        )
    )
}

@Composable
internal fun rememberAlgorithm(
    credentials: ServiceAccountCredentials = rememberGoogleCredentials()
): JwtAlgorithm = remember(credentials) {
    val jwkProvider = UrlJwkProvider(URL(APP_CHECK_PUBLIC_KEY))
    val privateKey = credentials.privateKey as RSAPrivateKey

    JwtAlgorithm.RSA256(Algorithm.Provider(jwkProvider, privateKey))
}

private inline operator fun <reified T : Any, reified R : Any> T.invoke(name: String): R =
    T::class.java.getDeclaredMethod(name)
        .also { it.isAccessible = true }
        .invoke(this) as R
