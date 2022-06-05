package io.ashdavies.playground.check

import com.google.cloud.functions.HttpRequest
import io.ashdavies.playground.cloud.HttpException
import io.ashdavies.playground.firebase.FirebaseFunction
import io.ashdavies.playground.firebase.appCheck

private const val APP_CHECK_TOKEN_KEY = "APP_CHECK_TOKEN"

/**
 * @see [Implement a custom App Check provider](https://firebase.google.com/docs/app-check/custom-provider)
 */
internal class AppCheckFunction : FirebaseFunction() {
    override suspend fun service(request: HttpRequest): String {
        val authenticity = AppCheckRequest(request)
        if (!authenticity.isValid()) {
            throw HttpException.Forbidden()
        }

        //val expiresAt = currentTimeMillis() / 1000 + 3600

        return firebaseApp
            .appCheck()
            .createToken(authenticity.appId)
            .token
    }
}

private fun AppCheckRequest.isValid(): Boolean =
    token == System.getenv(APP_CHECK_TOKEN_KEY)
