package io.ashdavies.check

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse
import io.ashdavies.firebase.rememberAppCheck
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.cloud.LocalHttpRequest
import io.ashdavies.playground.cloud.LocalHttpResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME

private const val APP_CHECK_TOKEN_KEY = "APP_CHECK_TOKEN"

private val expiresAt: ZonedDateTime
    get() = ZonedDateTime.now()
        .plusHours(1)

internal class AppCheckFunction : HttpFunction by HttpApplication({
    val request: AppCheckRequest = rememberAppCheckRequest()
    val response: HttpResponse = LocalHttpResponse.current
    val appCheck: AppCheck = rememberAppCheck()

    HttpEffect {
        if (!request.isValid()) throw HttpException.Forbidden("Bad authenticity")
        response.appendHeader("Expires", expiresAt.format(RFC_1123_DATE_TIME))

        appCheck
            .createToken(request.appId)
            .token
    }
})

@Composable
private fun rememberAppCheckRequest(request: HttpRequest = LocalHttpRequest.current): AppCheckRequest =
    remember(request) { AppCheckRequest(request) }

private fun AppCheckRequest.isValid(): Boolean =
    token == System.getenv(APP_CHECK_TOKEN_KEY)
