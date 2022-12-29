package io.ashdavies.check

import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient

public class AppCheck internal constructor(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectId: String,
) : AppCheckGenerator by AppCheckGenerator(
    httpClient = httpClient,
    cryptoSigner = cryptoSigner,
    projectId = projectId,
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
        client = httpClient,
        app = firebaseApp,
    ),
)
