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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

@OptIn(ExperimentalSerializationApi::class)
internal class AppCheckFunction : HttpFunction by HttpApplication(
    config = HttpConfig.Post,
    block = { appCheck() },
)

@Composable
@ExperimentalSerializationApi
private fun HttpScope.appCheck(
    firebaseApp: FirebaseApp = LocalFirebaseAdminApp.current,
    httpRequest: HttpRequest = LocalHttpRequest.current,
    httpClient: HttpClient = LocalHttpClient.current,
) = HttpEffect {
    val appCheckRequest = Json.decodeFromStream<AppCheckRequest>(httpRequest.inputStream)
    val appCheck = firebaseApp.appCheck(httpClient)

    val response = appCheck.createToken(
        appId = appCheckRequest.appId,
    )

    Json.encodeToString(response)
}
