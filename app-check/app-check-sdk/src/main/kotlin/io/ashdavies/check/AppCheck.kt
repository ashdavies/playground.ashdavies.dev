package io.ashdavies.check

import com.google.firebase.FirebaseApp
import io.ashdavies.http.DefaultHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth

public class AppCheck internal constructor(
    httpClient: HttpClient,
    projectId: String,
    cryptoSigner: CryptoSigner,
) : AppCheckGenerator by AppCheckGenerator(
    httpClient = httpClient,
    projectId = projectId,
    cryptoSigner = cryptoSigner,
),
    AppCheckVerifier by AppCheckVerifier(
        cryptoSigner = cryptoSigner,
    )

internal fun AppCheck(
    firebaseApp: FirebaseApp,
    httpClient: HttpClient,
    projectId: String,
): AppCheck = AppCheck(
    httpClient = httpClient,
    projectId = projectId,
    cryptoSigner = CryptoSigner(
        client = DefaultHttpClient(),
        app = firebaseApp,
    ),
)
