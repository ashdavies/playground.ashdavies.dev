package io.ashdavies.check

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.functions.HttpFunction
import io.ashdavies.check.AppCheckConstants.APP_CHECK_TOKEN_KEY
import io.ashdavies.playground.cloud.HttpApplication
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.hours

internal class AppCheckFunction : HttpFunction by HttpApplication({
    val credentials: ServiceAccountCredentials = rememberGoogleCredentials()
    val request: AppCheckRequest = rememberAppCheckRequest()
    val appCheck: AppCheckGenerator = rememberAppCheck()

    HttpEffect {
        if (request.token != System.getenv(APP_CHECK_TOKEN_KEY)) {
            throw HttpException.Forbidden("Bad authenticity")
        }

        appCheck.createToken(request) {
            issuer = credentials.clientEmail
            expiresAt = now() + 1.hours
            appId = request.appId
        }.token
    }
})
