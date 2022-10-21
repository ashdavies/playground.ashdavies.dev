package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.firebase.FirebaseApp
import io.ashdavies.compose.AuthorisedHttpApplication
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.LocalFirebaseApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock.System.now
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.time.Duration.Companion.hours

internal class AppCheckFunction : HttpFunction by AuthorisedHttpApplication({
    val appCheckRequest = rememberAppCheckRequest()
    val cryptoSigner = rememberCryptoSigner()
    val projectId = rememberProjectId()
    val appCheck = rememberAppCheck()

    HttpEffect {
        val appId = URLDecoder.decode(appCheckRequest.appId, StandardCharsets.UTF_8.name())
        val token = AppCheckToken.Request.Raw(projectId, appId)

        val response = appCheck.createToken(token) {
            it.issuer = cryptoSigner.getAccountId()
            it.expiresAt = now() + 1.hours
            it.appId = token.appId
        }

        response.token
    }
})

@Composable
private fun rememberAppCheckRequest(
    httpRequest: HttpRequest = LocalHttpRequest.current
): AppCheckRequest = remember(httpRequest) {
    AppCheckRequest(httpRequest)
}

@Composable
private fun rememberCryptoSigner(
    firebaseApp: FirebaseApp = LocalFirebaseApp.current,
    httpClient: HttpClient = LocalHttpClient.current
): CryptoSigner = remember(firebaseApp, httpClient) {
    CryptoSigner(firebaseApp, httpClient)
}

@Composable
private fun rememberProjectId(
    firebaseApp: FirebaseApp = LocalFirebaseApp.current,
): String = remember(firebaseApp) {
    getProjectId(firebaseApp)
}

@Composable
private fun rememberAppCheck(
    client: HttpClient = LocalHttpClient.current,
    signer: CryptoSigner = rememberCryptoSigner()
): AppCheck = remember(client, signer) {
    AppCheck(client, signer)
}
