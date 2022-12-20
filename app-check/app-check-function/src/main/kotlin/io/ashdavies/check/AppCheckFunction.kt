package io.ashdavies.check

import com.google.cloud.functions.HttpFunction
import io.ashdavies.compose.AuthorisedHttpApplication
import io.ashdavies.http.LocalHttpClient
import io.ashdavies.playground.cloud.HttpConfig
import io.ashdavies.playground.cloud.HttpEffect
import io.ashdavies.playground.cloud.LocalFirebaseAdminApp
import io.ashdavies.playground.cloud.LocalHttpRequest
import kotlinx.datetime.Clock.System.now
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.time.Duration.Companion.hours

private fun urlDecode(value: String, charset: Charset = StandardCharsets.UTF_8): String {
    return URLDecoder.decode(value, charset.name())
}

internal class AppCheckFunction : HttpFunction by AuthorisedHttpApplication(HttpConfig.Post, {
    val firebaseApp = LocalFirebaseAdminApp.current
    val httpRequest = LocalHttpRequest.current
    val httpClient = LocalHttpClient.current

    HttpEffect {
        val cryptoSigner = CryptoSigner(firebaseApp, httpClient)
        val appCheckRequest = AppCheckRequest(httpRequest)
        val appCheck = AppCheck(httpClient, cryptoSigner)
        val projectId = getProjectId(firebaseApp)

        val token = AppCheckToken.Request.Raw(
            appId = urlDecode(appCheckRequest.appId),
            projectId = projectId,
        )

        val response = appCheck.createToken(token) {
            it.issuer = cryptoSigner.getAccountId()
            it.expiresAt = now() + 1.hours
            it.appId = token.appId
        }

        Json.encodeToString(response)
    }
},)
