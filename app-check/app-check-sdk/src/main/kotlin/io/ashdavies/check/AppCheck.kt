package io.ashdavies.check

import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient

public class AppCheck internal constructor(
    httpClient: HttpClient,
    projectId: String,
    cryptoSigner: CryptoSigner,
) : AppCheckGenerator by AppCheckGenerator(
    httpClient = httpClient,
    projectId = projectId,
    cryptoSigner = cryptoSigner,
), AppCheckVerifier by AppCheckVerifier(
    cryptoSigner = cryptoSigner,
)

internal fun AppCheck(
    firebaseApp: FirebaseApp,
    httpClient: HttpClient,
    projectId: String,
): AppCheck = AppCheck(
    cryptoSigner = CryptoSigner(firebaseApp, httpClient),
    httpClient = httpClient,
    projectId = projectId,
)
