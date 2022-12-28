package io.ashdavies.check

import io.ktor.client.HttpClient

public class AppCheck(httpClient: HttpClient, cryptoSigner: CryptoSigner, projectId: String) :
    AppCheckGenerator by AppCheckGenerator(httpClient, cryptoSigner, projectId),
    AppCheckVerifier by AppCheckVerifier(cryptoSigner)
