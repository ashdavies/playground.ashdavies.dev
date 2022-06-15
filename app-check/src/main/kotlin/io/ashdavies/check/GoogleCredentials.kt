package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ashdavies.playground.cloud.LocalFirebaseApp

private const val GET_CREDENTIALS_METHOD = "getCredentials"

@Composable
internal inline fun <reified T : GoogleCredentials> rememberGoogleCredentials(app: FirebaseApp = LocalFirebaseApp.current): T {
    return remember(app.name) { app.options<FirebaseOptions, T>(GET_CREDENTIALS_METHOD) }
}

private inline operator fun <reified T : Any, reified R : Any> T.invoke(name: String): R {
    return T::class.java.getDeclaredMethod(name)
        .also { it.isAccessible = true }
        .invoke(this) as R
}
