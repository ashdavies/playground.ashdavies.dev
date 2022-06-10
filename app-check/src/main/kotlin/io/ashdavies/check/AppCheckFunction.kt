package io.ashdavies.check

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpResponse
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.cloud.LocalHttpResponse
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
import kotlin.time.Duration.Companion.hours

private const val APP_CHECK_TOKEN_KEY = "APP_CHECK_TOKEN"

private val expiresAt: Instant
    get() = Clock.System.now()
        .plus(1.hours)

@Suppress("unused")
internal class AppCheckFunction : HttpFunction by HttpApplication({
    val credentials: ServiceAccountCredentials = rememberServiceAccountCredentials()
    val request: AppCheckRequest = rememberAppCheckRequest()
    val response: HttpResponse = LocalHttpResponse.current
    val appCheck: AppCheck = rememberAppCheck()

    HttpEffect {
        if (!request.isValid()) throw HttpException.Forbidden("Bad authenticity")
        response.appendHeader("Expires", expiresAt.asRfcString)

        val options = AppCheckTokenOptions(
            issuer = credentials.clientEmail,
            appId = request.appId,
            expiresAt = expiresAt,
        )

        appCheck
            .createToken(request.appId, options)
            .token
    }
})

private val Instant.asRfcString: String
    get() = toJavaInstant()
        .atZone(ZoneId.systemDefault())
        .format(RFC_1123_DATE_TIME)

private fun AppCheckRequest.isValid(): Boolean =
    token == System.getenv(APP_CHECK_TOKEN_KEY)
