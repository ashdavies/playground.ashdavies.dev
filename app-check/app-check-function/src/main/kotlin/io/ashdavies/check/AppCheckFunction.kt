package io.ashdavies.check

import androidx.compose.runtime.remember
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import io.ashdavies.compose.AuthorisedHttpApplication
import io.ashdavies.compose.rememberAppCheck
import io.ashdavies.compose.rememberCryptoSigner
import io.ashdavies.compose.rememberProjectId
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.cloud.LocalHttpRequest
import kotlinx.datetime.Clock.System.now
import kotlin.time.Duration.Companion.hours

private const val APP_CHECK_KEY = "APP_CHECK_KEY"
private const val BAD_AUTHENTICITY = "Bad authenticity"

internal class AppCheckFunction : HttpFunction by AuthorisedHttpApplication({
    val request: HttpRequest = LocalHttpRequest.current
    val query: AppCheckQuery = remember(request) {
        AppCheckQuery(request)
    }

    val signer: CryptoSigner = rememberCryptoSigner()
    val appCheck: AppCheck = rememberAppCheck()
    val projectId: String = rememberProjectId()

    HttpEffect {
        if (query.appKey != System.getenv(APP_CHECK_KEY)) {
            throw HttpException.Forbidden(BAD_AUTHENTICITY)
        }

        val token = AppCheckToken.Request.Raw(projectId, query.appId)
        val response = appCheck.createToken(token) {
            it.issuer = signer.getAccountId()
            it.expiresAt = now() + 1.hours
            it.appId = token.appId
        }

        response.token
    }
})
