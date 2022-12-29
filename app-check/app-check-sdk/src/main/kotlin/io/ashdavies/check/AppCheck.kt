package io.ashdavies.check

import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient

public class AppCheck internal constructor(
    httpClient: HttpClient,
    projectId: String,
    cryptoSigner: CryptoSigner,
    appId: String,
) : AppCheckGenerator by AppCheckGenerator(
    httpClient = AuthorisedHttpClient(
        config = HttpClientConfig(
            algorithm = GoogleAlgorithm(cryptoSigner),
            accountId = cryptoSigner.getAccountId(),
            appId = appId,
        ),
        from = httpClient,
    ),
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
    appId: String,
): AppCheck = AppCheck(
    httpClient = httpClient,
    projectId = projectId,
    appId = appId,
    cryptoSigner = CryptoSigner(
        client = httpClient,
        app = firebaseApp,
    ),
)
