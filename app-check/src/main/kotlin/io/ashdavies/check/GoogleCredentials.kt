package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.compose.Provides

private const val GET_CREDENTIALS_METHOD = "getCredentials"

@Composable
internal fun rememberAccountSigner(app: FirebaseApp = LocalFirebaseApp.current): ServiceAccountSigner {
    return when (val credentials: GoogleCredentials = rememberGoogleCredentials(app)) {
        is ServiceAccountSigner -> credentials
        else -> throw IllegalStateException()
    }
}

@Provides
@Composable
internal fun rememberGoogleCredentials(app: FirebaseApp = LocalFirebaseApp.current): GoogleCredentials = remember(app) {
    FirebaseOptions::class.java
        .getDeclaredMethod(GET_CREDENTIALS_METHOD)
        .also { it.isAccessible = true }
        .invoke(app.options) as GoogleCredentials
}
