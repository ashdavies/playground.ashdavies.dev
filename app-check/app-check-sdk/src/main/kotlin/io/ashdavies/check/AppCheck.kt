package io.ashdavies.check

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient

public class AppCheck(client: HttpClient, algorithm: Algorithm) :
    AppCheckGenerator by AppCheckGenerator(client, algorithm),
    AppCheckVerifier by AppCheckVerifier(algorithm)
