package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.hours

private const val APP_CHECK_KEY = "APP_CHECK_KEY"
private const val BAD_AUTHENTICITY = "Bad authenticity"

internal class AppCheckFunction : HttpFunction by AuthorisedHttpApplication({
    val query: AppCheckQuery = rememberAppCheckQuery()
    val signer: CryptoSigner = rememberCryptoSigner()
    val appCheck: AppCheck = rememberAppCheck()
    val projectId: String = rememberProjectId()

    HttpEffect {
        if (query.appKey != System.getenv(APP_CHECK_KEY)) {
            throw HttpException.Forbidden(BAD_AUTHENTICITY)
        }

        val request = AppCheckToken.Request.Raw(projectId, query.appId)
        val response = appCheck.createToken(request) {
            it.issuer = signer.getAccountId()
            it.expiresAt = now() + 1.hours
            it.appId = request.appId
        }

        response.token
    }
})
