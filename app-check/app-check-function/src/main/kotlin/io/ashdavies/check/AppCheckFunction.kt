package io.ashdavies.check

import androidx.compose.runtime.Composable
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.firebase.FirebaseApp
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpConfig
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpScope
import io.ashdavies.playground.cloud.LocalFirebaseAdminApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ktor.client.HttpClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class AppCheckFunction : HttpFunction by HttpApplication(
    config = HttpConfig.Post,
    block = { appCheck() },
)

@Composable
private fun HttpScope.appCheck(
    firebaseApp: FirebaseApp = LocalFirebaseAdminApp.current,
    httpRequest: HttpRequest = LocalHttpRequest.current,
    httpClient: HttpClient = LocalHttpClient.current,
) = HttpEffect {
    val appCheckRequest = AppCheckRequest(httpRequest)
    val appCheck = firebaseApp.appCheck(httpClient)

    val response = appCheck.createToken(
        appId = appCheckRequest.appId,
    )

    Json.encodeToString(response)
}
