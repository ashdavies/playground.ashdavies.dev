package io.ashdavies.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.cloud.functions.HttpMessage
import io.ashdavies.check.AppCheck
import io.ashdavies.check.appCheck
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.cloud.HttpScope
import io.ashdavies.playground.cloud.LocalApplicationScope
import io.ashdavies.playground.cloud.LocalFirebaseAdminApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.LocalHttpResponse
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.coroutines.CoroutineScope

private const val APP_CHECK_HEADER = "X-Firebase-AppCheck"

private val HttpMessage.appCheckToken: String?
    get() = headers[APP_CHECK_HEADER]?.firstOrNull()

@Composable
public fun HttpScope.VerifiedHttpEffect(block: suspend CoroutineScope.() -> String) {
    var isVerified by remember { mutableStateOf(false) }

    if (!isVerified) {
        val applicationScope = LocalApplicationScope.current
        val firebaseApp = LocalFirebaseAdminApp.current
        val httpResponse = LocalHttpResponse.current
        val httpRequest = LocalHttpRequest.current
        val httpClient = LocalHttpClient.current

        val appCheck: AppCheck = remember(firebaseApp, httpClient) {
            firebaseApp.appCheck(httpClient)
        }

        LaunchedEffect(Unit) {
            try {
                appCheck.verifyToken(
                    token = httpRequest.appCheckToken ?: throw HttpException.InvalidToken(),
                    mapper = { _, _, _, _, _, _ -> isVerified = true },
                )
            } catch (exception: HttpException) {
                httpResponse.setStatusCode(Unauthorized.value, "Unauthorized")
                applicationScope.exitApplication()
            }
        }
    }

    if (isVerified) {
        HttpEffect(block)
    }
}

private fun HttpException.Companion.InvalidToken() = Forbidden("Invalid app check token")
