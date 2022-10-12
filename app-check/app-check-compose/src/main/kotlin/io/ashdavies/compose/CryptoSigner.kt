package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.firebase.FirebaseApp
import io.ashdavies.check.CryptoSigner
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ktor.client.HttpClient

@Composable
public fun rememberCryptoSigner(
    app: FirebaseApp = LocalFirebaseApp.current,
    client: HttpClient = LocalHttpClient.current,
): CryptoSigner = remember(app, client) {
    CryptoSigner(app, client)
}
