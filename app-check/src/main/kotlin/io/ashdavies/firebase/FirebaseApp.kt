package io.ashdavies.firebase

import com.auth0.jwt.algorithms.Algorithm
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import io.ashdavies.check.AppCheck
import io.ashdavies.check.AppCheckClient
import io.ashdavies.check.AppCheckConfig
import io.ktor.client.HttpClient
import java.security.interfaces.RSAPrivateKey

internal fun FirebaseApp.appCheck(): AppCheck {
    val credentials = GoogleCredentials.getApplicationDefault() as ServiceAccountCredentials
    val algorithm = Algorithm.RSA256(null, credentials.privateKey as RSAPrivateKey)
    val client = HttpClient()

    return AppCheck(
        client = AppCheckClient(client, credentials.projectId),
        config = AppCheckConfig(algorithm, credentials.clientEmail),
    )
}
