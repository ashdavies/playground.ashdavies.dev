package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.auth.oauth2.ComputeEngineCredentials
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.compose.Provides
import java.security.interfaces.RSAPrivateKey
import kotlinx.serialization.Serializable

private const val GET_CREDENTIALS_METHOD = "getCredentials"

@Provides
@Composable
internal inline fun <reified T : GoogleCredentials> rememberGoogleCredentials(app: FirebaseApp = LocalFirebaseApp.current): T {
    return remember(app.name) { app.options<FirebaseOptions, T>(GET_CREDENTIALS_METHOD) }
}

private inline operator fun <reified T : Any, reified R : Any> T.invoke(name: String): R {
    return T::class.java.getDeclaredMethod(name)
        .also { it.isAccessible = true }
        .invoke(this) as R
}

@Serializable
internal data class Credentials(val clientEmail: String, val projectId: String, val privateKey: RSAPrivateKey) {
    constructor(credentials: ComputeEngineCredentials) : this(
        privateKey = credentials.privateKey as RSAPrivateKey,
        clientEmail = credentials.clientEmail,
        projectId = credentials.projectId,
    )

    constructor(credentials: ServiceAccountCredentials) : this(
        privateKey = credentials.privateKey as RSAPrivateKey,
        clientEmail = credentials.clientEmail,
        projectId = credentials.projectId,
    )
}
