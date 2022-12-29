package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.ApplicationScope
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import com.google.firebase.FirebaseApp
import io.ashdavies.check.AppCheck
import io.ashdavies.check.appCheck
import io.ashdavies.check.appCheckToken
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.cloud.HttpScope
import io.ashdavies.playground.cloud.LocalApplicationScope
import io.ashdavies.playground.cloud.LocalFirebaseAdminApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.LocalHttpResponse
import io.ashdavies.playground.cloud.getValue
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope

private const val APP_CHECK_ENDPOINT = "https://firebaseappcheck.googleapis.com/"

@Composable
public fun HttpScope.VerifiedHttpEffect(block: suspend CoroutineScope.() -> String) {
    var isVerified by remember { mutableStateOf(false) }

    if (!isVerified) {
        val scope: ApplicationScope = LocalApplicationScope.current
        val response: HttpResponse = LocalHttpResponse.current
        val request: HttpRequest = LocalHttpRequest.current
        val appId: String by LocalHttpRequest.current
        val appCheck: AppCheck = rememberAppCheck(
            appId = appId,
        )

        LaunchedEffect(Unit) {
            try {
                val appCheckToken = request.appCheckToken
                    ?: throw HttpException.Forbidden("Unauthorized")

                appCheck.verifyToken(appCheckToken) {
                    issuer = "${APP_CHECK_ENDPOINT}${appId.split(":")[1]}"
                }

                isVerified = true
            } catch (exception: HttpException) {
                response.setStatusCode(401, "Unauthorized")
                scope.exitApplication()
            }
        }
    }

    if (isVerified) {
        HttpEffect(block)
    }
}

@Composable
private fun rememberAppCheck(
    firebaseApp: FirebaseApp = LocalFirebaseAdminApp.current,
    httpClient: HttpClient = LocalHttpClient.current,
    appId: String,
): AppCheck = remember(firebaseApp, httpClient) {
    firebaseApp.appCheck(httpClient, appId)
}
