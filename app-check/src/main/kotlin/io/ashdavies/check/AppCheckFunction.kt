package io.ashdavies.check

import com.google.auth.ServiceAccountSigner
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.functions.HttpFunction
import io.ashdavies.check.AppCheckConstants.APP_CHECK_ENDPOINT
import io.ashdavies.check.AppCheckConstants.APP_CHECK_KEY
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.hours

internal class AppCheckFunction : HttpFunction by AuthorizedHttpApplication({
    val credentials: ServiceAccountCredentials = rememberGoogleCredentials() as ServiceAccountCredentials
    val signer: ServiceAccountSigner = rememberAccountSigner()
    val query: AppCheckQuery = rememberAppCheckRequest()
    val appCheck: AppCheck = rememberAppCheck()

    HttpEffect {
        if (query.appKey != System.getenv(APP_CHECK_KEY)) {
            throw HttpException.Forbidden("Bad authenticity")
        }

        val request = AppCheckToken.Request.Raw(query.appId, credentials.projectId)
        val projectNumber = query.appId.split(":")[1]

        val token = appCheck.createToken(request) {
            it.expiresAt = now() + 1.hours
            it.issuer = signer.account
            it.appId = request.appId
        }.token

        appCheck.verifyToken(token) {
            issuer = "${APP_CHECK_ENDPOINT}${projectNumber}"
        }.token
    }
})
