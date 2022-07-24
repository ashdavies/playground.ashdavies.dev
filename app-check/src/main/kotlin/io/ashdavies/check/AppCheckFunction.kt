package io.ashdavies.check

import com.google.auth.ServiceAccountSigner
import com.google.cloud.functions.HttpFunction
import com.google.firebase.FirebaseApp
import io.ashdavies.check.AppCheckConstants.APP_CHECK_ENDPOINT
import io.ashdavies.check.AppCheckConstants.APP_CHECK_KEY
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.cloud.LocalFirebaseApp
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.hours

private val AppCheckQuery.projectNumber: String
    get() = appId.split(":")[1]

@Suppress("unused")
internal class AppCheckFunction : HttpFunction by AuthorizedHttpApplication({
    val signer: ServiceAccountSigner = rememberAccountSigner()
    val query: AppCheckQuery = rememberAppCheckRequest()
    val app: FirebaseApp = LocalFirebaseApp.current
    val appCheck: AppCheck = rememberAppCheck()

    HttpEffect {
        if (query.appKey != System.getenv(APP_CHECK_KEY)) {
            throw HttpException.Forbidden("Bad authenticity")
        }

        val request = AppCheckToken.Request.Raw(query.appId, app.options.projectId)
        val token = appCheck.createToken(request) {
            it.expiresAt = now() + 1.hours
            it.issuer = signer.account
            it.appId = request.appId
        }.token

        appCheck
            .verifyToken(token) { issuer = "${APP_CHECK_ENDPOINT}${query.projectNumber}" }
            .token
    }
})
