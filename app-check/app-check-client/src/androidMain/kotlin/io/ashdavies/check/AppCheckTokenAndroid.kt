package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import io.ashdavies.compose.LocalFirebaseAndroidApp
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.http.header
import io.ktor.client.HttpClient
import com.google.firebase.appcheck.AppCheckToken as FirebaseAppCheckToken

private const val X_FIREBASE_APP_CHECK = "X-Firebase-AppCheck"

@Composable
public actual fun ProvideAppCheckToken(client: HttpClient, content: @Composable () -> Unit) {
    val factory = PlayIntegrityAppCheckProviderFactory.getInstance()
    val firebaseApp = LocalFirebaseAndroidApp.current
    val appCheck = firebaseApp.appCheck

    val token by produceState<AppCheckToken?>(null) {
        appCheck.addAppCheckListener { value = AppCheckToken(it) }
    }

    appCheck.installAppCheckProviderFactory(factory)

    CompositionLocalProvider(
        LocalHttpClient provides client.header(X_FIREBASE_APP_CHECK, token),
        content = content,
    )
}

private fun AppCheckToken(from: FirebaseAppCheckToken) = AppCheckToken(
    expireTimeMillis = from.expireTimeMillis,
    token = from.token,
)
