package io.ashdavies.check

import io.ktor.client.HttpClient

public class AppCheck internal constructor(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectId: String,
    appId: String,
) : AppCheckGenerator by AppCheckGenerator(
    httpClient = httpClient,
    cryptoSigner = cryptoSigner,
    projectId = projectId,
    appId = appId,
),
    AppCheckVerifier by AppCheckVerifier(
        cryptoSigner = cryptoSigner,
    )
