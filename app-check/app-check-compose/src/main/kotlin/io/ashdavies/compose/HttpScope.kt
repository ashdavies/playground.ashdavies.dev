package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope

private const val APP_CHECK_ENDPOINT = "https://firebaseappcheck.googleapis.com/"

@Composable
public fun HttpScope.VerifiedHttpEffect(block: suspend CoroutineScope.() -> String) {
    var isVerified by remember { mutableStateOf(false) }

    if (!isVerified) {
        val applicationScope = LocalApplicationScope.current
        val firebaseApp = LocalFirebaseAdminApp.current
        val httpResponse = LocalHttpResponse.current
        val httpRequest = LocalHttpRequest.current
        val httpClient = LocalHttpClient.current
        val appId by LocalHttpRequest.current

        val appCheck: AppCheck = remember(firebaseApp, httpClient) {
            firebaseApp.appCheck(httpClient, appId)
        }

        LaunchedEffect(Unit) {
            try {
                val appCheckToken = httpRequest.appCheckToken ?: throw HttpException.Forbidden(
                    message = "Unauthorized",
                )

                appCheck.verifyToken(appCheckToken) {
                    issuer = "${APP_CHECK_ENDPOINT}${appId.split(":")[1]}"
                }

                isVerified = true
            } catch (exception: HttpException) {
                httpResponse.setStatusCode(401, "Unauthorized")
                applicationScope.exitApplication()
            }
        }
    }

    if (isVerified) {
        HttpEffect(block)
    }
}
