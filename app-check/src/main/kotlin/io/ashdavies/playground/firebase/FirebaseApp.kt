package io.ashdavies.playground.firebase

import com.auth0.jwt.algorithms.Algorithm
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import io.ashdavies.playground.check.AppCheck
import io.ashdavies.playground.check.AppCheckClient
import io.ashdavies.playground.check.AppCheckConfig
import io.ktor.client.HttpClient
import java.security.interfaces.RSAPrivateKey

internal fun FirebaseApp.appCheck(): AppCheck {
    val credentials = GoogleCredentials.getApplicationDefault() as ServiceAccountCredentials
    val algorithm = Algorithm.RSA256(null, credentials.privateKey as RSAPrivateKey)

    return AppCheck(
        config = AppCheckConfig(algorithm, options.serviceAccountId),
        client = AppCheckClient(HttpClient(), options.projectId)
    )
}
