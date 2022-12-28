package io.ashdavies.check

import io.ktor.client.HttpClient

public class AppCheck internal constructor(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectId: String,
    appId: String
) : AppCheckGenerator by AppCheckGenerator(
    httpClient = AuthorisedHttpClient(
        config = HttpClientConfig(
            accountId = cryptoSigner.getAccountId(),
            algorithm = GoogleAlgorithm(cryptoSigner),
            appId = appId,
        ),
        from = httpClient,
    ),
    cryptoSigner = cryptoSigner,
    projectId = projectId,
    appId = appId,
), AppCheckVerifier by AppCheckVerifier(
    cryptoSigner = cryptoSigner
)
