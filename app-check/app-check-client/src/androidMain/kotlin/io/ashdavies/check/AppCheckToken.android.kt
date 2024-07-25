package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import com.google.firebase.appcheck.AppCheckToken
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import io.ashdavies.http.AppCheckToken
import io.ashdavies.http.ProvideHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@Composable
public actual fun ProvideAppCheckToken(client: HttpClient, content: @Composable () -> Unit) {
    val appCheck = remember { FirebaseAppCheck.getInstance() }
    val token by produceState<AppCheckToken?>(null) {
        appCheck.appCheckToken().collect { value = it }
    }

    LaunchedEffect(appCheck) {
        val factory = PlayIntegrityAppCheckProviderFactory.getInstance()
        appCheck.installAppCheckProviderFactory(factory)
    }

    ProvideHttpClient(
        config = {
            install(DefaultRequest) {
                header(HttpHeaders.AppCheckToken, token?.token)
            }
        },
        content = content,
    )
}

private fun FirebaseAppCheck.appCheckToken(): Flow<AppCheckToken> = channelFlow {
    val appCheckListener = FirebaseAppCheck.AppCheckListener { trySend(it) }
    addAppCheckListener(appCheckListener)

    invokeOnClose {
        removeAppCheckListener(appCheckListener)
    }
}
