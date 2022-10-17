package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import com.google.firebase.FirebaseApp
import io.ktor.client.HttpClient

public class AppCheck(client: HttpClient, algorithm: Algorithm) :
    AppCheckGenerator by AppCheckGenerator(client, algorithm),
    AppCheckVerifier by AppCheckVerifier(algorithm)

public fun AppCheck(client: HttpClient, signer: CryptoSigner): AppCheck =
    AppCheck(client, GoogleAlgorithm(signer))

public fun AppCheck(app: FirebaseApp, client: HttpClient): AppCheck =
    AppCheck(client, CryptoSigner(app, client))
