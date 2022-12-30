package io.ashdavies.check

import io.ktor.client.HttpClient

public class AppCheck internal constructor(
    httpClient: HttpClient,
    cryptoSigner: CryptoSigner,
    projectNumber: String,
    projectId: String,
) :
    AppCheckGenerator by AppCheckGenerator(
        httpClient = httpClient,
        cryptoSigner = cryptoSigner,
        projectId = projectId,
    ),
    AppCheckVerifier by AppCheckVerifier(
        projectNumber = projectNumber,
        cryptoSigner = cryptoSigner,
    )
