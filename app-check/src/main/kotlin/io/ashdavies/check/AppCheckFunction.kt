package io.ashdavies.check

import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.functions.HttpFunction
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.hours

private const val APP_CHECK_ENDPOINT = "https://firebaseappcheck.googleapis.com/"
private const val APP_CHECK_KEY = "APP_CHECK_KEY"

private const val BAD_AUTHENTICITY = "Bad authenticity"

internal class AppCheckFunction : HttpFunction by AuthorizedHttpApplication({
    val credentials: ServiceAccountCredentials = rememberGoogleCredentials() as ServiceAccountCredentials
    val signer: ServiceAccountSigner = rememberAccountSigner()
    val query: AppCheckQuery = rememberAppCheckRequest()
    val appCheck: AppCheck = rememberAppCheck()

    HttpEffect {
        if (query.appKey != System.getenv(APP_CHECK_KEY)) {
            throw HttpException.Forbidden(BAD_AUTHENTICITY)
        }

        val request = AppCheckToken.Request.Raw(query.appId, credentials.projectId)
        val response = appCheck.createToken(request) {
            it.expiresAt = now() + 1.hours
            it.issuer = signer.account
            it.appId = request.appId
        }

        val decoded = appCheck.verifyToken(response.token) {
            issuer = "${APP_CHECK_ENDPOINT}${query.appId.split(":")[1]}"
        }

        if (response.token == decoded.token) {
            println("WARNING: Performed unnecessary token validation")
        }

        decoded.token
    }
})
